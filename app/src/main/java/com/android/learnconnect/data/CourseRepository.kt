package com.android.learnconnect.data

import androidx.room.Query
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getAllCourseList(): ResultData<List<Course>>

    suspend fun registerCourse(courseId: String): ResultData<*>

    suspend fun getCourseDataById(courseId: String): ResultData<Course>

    suspend fun filterCourseByCategory(categoryName: String): ResultData<List<Course>>

    suspend fun searchCoursesByKeyword(keyword: String): ResultData<List<Course>>

    suspend fun getRegisteredCourse(): ResultData<List<Course>>

}
