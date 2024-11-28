package com.android.learnconnect.data.course

import com.android.learnconnect.data.locale.CourseDao
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.MockData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

class CourseDataSourceTest {

    private lateinit var courseDao: CourseDao
    private lateinit var courseDataSource: CourseDataSource

    @Before
    fun setUp() {
        courseDao = mock()
        courseDataSource = CourseDataSource(courseDao)
    }

    @Test
    fun `getAllCourseList returns all courses`() = runBlocking {
        val expectedCourses = listOf(
            MockData.mockData.get(0), MockData.mockData.get(1)
        )
        `when`(courseDao.getAllCourses()).thenReturn(expectedCourses)

        val result = courseDataSource.getAllCourseList()

        verify(courseDao).getAllCourses()
        assert(result is ResultData.Success)
        assertEquals(expectedCourses, (result as ResultData.Success).data)
    }

    @Test
    fun `registerCourse returns success when update is successful`() = runBlocking {
        val courseId = "1"
        `when`(courseDao.updateRegistration(courseId, true)).thenReturn(Unit)

        val result = courseDataSource.registerCourse(courseId)

        verify(courseDao).updateRegistration(courseId, true)
        assert(result is ResultData.Success)
    }

}
