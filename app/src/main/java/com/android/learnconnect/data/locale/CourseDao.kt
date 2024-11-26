package com.android.learnconnect.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.learnconnect.domain.entity.Course

@Dao
interface CourseDao {
    @Insert
    suspend fun insertAll(vararg courses: Course)

    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<Course>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: String): Course

    @Query("UPDATE courses SET isRegistered = :isRegistered WHERE id = :courseId")
    suspend fun updateRegistration(courseId: String, isRegistered: Boolean)

    @Query("SELECT * FROM courses WHERE isRegistered = 1")
    suspend fun getRegisteredCourseList(): List<Course>

    @Query("UPDATE courses SET isFavorite = :isFavorite WHERE id = :courseId")
    suspend fun setFavoriteCourse(courseId: String, isFavorite: Boolean)

    @Query("SELECT * FROM courses WHERE isFavorite = 1")
    suspend fun getFavoriteCourseList(): List<Course>

    @Query("SELECT * FROM courses WHERE LOWER(category) LIKE '%' || LOWER(:categoryName) || '%'")
    suspend fun filterCourseByCategory(categoryName: String): List<Course>

    @Query("SELECT * FROM courses WHERE LOWER(name) LIKE '%' || LOWER(:keyword) || '%'")
    suspend fun searchCoursesByKeyword(keyword: String): List<Course>
}