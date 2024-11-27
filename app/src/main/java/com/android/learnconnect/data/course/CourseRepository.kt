package com.android.learnconnect.data.course

import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData

interface CourseRepository {
    suspend fun getAllCourseList(): ResultData<List<Course>>

    suspend fun registerCourse(courseId: String): ResultData<*>

    suspend fun getCourseDataById(courseId: String): ResultData<Course>

    suspend fun filterCourseByCategory(categoryName: String): ResultData<List<Course>>

    suspend fun searchCoursesByKeyword(keyword: String): ResultData<List<Course>>

    suspend fun getRegisteredCourse(): ResultData<List<Course>>

    suspend fun setFavoriteCourse(courseId: String, isFavorite: Boolean): ResultData<*>

    suspend fun getFavoriteCourseList(): ResultData<List<Course>>

    suspend fun setLastSecondOfVide(courseId: String, videoId: String, lastSecond: Int) : ResultData<*>

}
