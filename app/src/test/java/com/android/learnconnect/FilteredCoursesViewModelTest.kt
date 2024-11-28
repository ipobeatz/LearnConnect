package com.android.learnconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.learnconnect.data.category.CategoryRepository
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.ui.categorydetail.FilteredCoursesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FilteredCoursesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: FilteredCoursesViewModel
    private lateinit var mockCategoryRepository: CategoryRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockCategoryRepository = mockk(relaxed = true)
        viewModel = FilteredCoursesViewModel(mockCategoryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testGetCategoriesSuccess() = testDispatcher.runBlockingTest {
        val fakeCategories = listOf(Category("1", "Software Development", "Learn all about software development."), Category("2", "Personal Development", "Improve your personal skills."))
        val flow = flowOf(ResultData.Success(fakeCategories))

        coEvery { mockCategoryRepository.getCategoryList() } returns flow

        // LiveData observation
        viewModel.getCategories().observeForever { result ->
            when (result) {
                is ResultData.Success -> {
                    assertEquals(fakeCategories, result.data)
                }
                else -> fail("Expected ResultData.Success")
            }
        }
    }
}
