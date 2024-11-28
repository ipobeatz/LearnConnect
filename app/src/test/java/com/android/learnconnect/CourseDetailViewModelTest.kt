package com.android.learnconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.entity.VideoItem
import com.android.learnconnect.domain.usecase.GetCourseByIdUseCase
import com.android.learnconnect.domain.usecase.RegisterCourseUseCase
import com.android.learnconnect.domain.usecase.SetFavoriteCourseUseCase
import com.android.learnconnect.domain.usecase.UpdateLastSecondOfVideoUseCase
import com.android.learnconnect.ui.coursedetail.CourseDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs // Correct import for assertIs

@ExperimentalCoroutinesApi
class CourseDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CourseDetailViewModel
    private lateinit var mockGetCourseByIdUseCase: GetCourseByIdUseCase
    private lateinit var mockRegisterCourseUseCase: RegisterCourseUseCase
    private lateinit var mockSetFavoriteCourseUseCase: SetFavoriteCourseUseCase
    private lateinit var mockUpdateLastSecondOfVideoUseCase: UpdateLastSecondOfVideoUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockGetCourseByIdUseCase = mockk(relaxed = true)
        mockRegisterCourseUseCase = mockk(relaxed = true)
        mockSetFavoriteCourseUseCase = mockk(relaxed = true)
        mockUpdateLastSecondOfVideoUseCase = mockk(relaxed = true)

        viewModel = CourseDetailViewModel(
            setFavoriteCourseUseCase = mockSetFavoriteCourseUseCase,
            getCourseByIdUseCase = mockGetCourseByIdUseCase,
            registerCourseUseCase = mockRegisterCourseUseCase,
            updateLastSecondOfVideoUseCase = mockUpdateLastSecondOfVideoUseCase
        )
    }

    @Test
    fun `get course data from id emits correct data`() = runTest {
        val courseId = "2"
        val fakeVideoItems = listOf(
            VideoItem("test1", "Introduction to Java", "120", 120,"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
            VideoItem("test2", "Java OOP Concepts", "90", 150,"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        )
        val fakeCourse = Course(
            id = courseId,
            name = "Advanced Java Programming",
            description = "Master Java programming concepts and techniques.",
            imageUrl = "https://i.ibb.co/hCwsrMK/studio.png",
            isRegistered = false,
            isFavorite = false,
            rating = 4.0f,
            coursePrice = 39.99f,
            category = "Yazılım Geliştirme",
            videoItem = fakeVideoItems
        )
        val flow = flowOf(ResultData.Success(fakeCourse))

        coEvery { mockGetCourseByIdUseCase.invoke(courseId) } returns flow

        viewModel.getCourseDataFromId(courseId)

        advanceUntilIdle() // Tüm coroutine'lerin tamamlanmasını sağlar

        assertTrue(viewModel.courseData.value is ResultData.Success<*>)

        (viewModel.courseData.value as ResultData.Success<*>).data.let { data ->
            assertTrue(data is Course)
            assertEquals(fakeCourse, data)
        }
    }
}