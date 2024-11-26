package com.android.learnconnect.data

import com.android.learnconnect.data.locale.CourseDao
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CourseDataSource @Inject constructor(
    private val courseDao: CourseDao
) : CourseRepository {

    override suspend fun getAllCourseList(): ResultData<List<Course>> {
        return ResultData.Success(courseDao.getAllCourses())
    }

    override suspend fun registerCourse(courseId: String): ResultData<*> {
        return try {
            ResultData.Success(courseDao.updateRegistration(courseId, true))
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun getCourseDataById(courseId: String): ResultData<Course> {
        return ResultData.Success(courseDao.getCourseById(courseId))
    }

    override suspend fun filterCourseByCategory(categoryName: String): ResultData<List<Course>> {
        return ResultData.Success(courseDao.filterCourseByCategory(categoryName))
    }

    override suspend fun searchCoursesByKeyword(keyword: String): ResultData<List<Course>> {
        return ResultData.Success(courseDao.getAllCourses())
    }

    override suspend fun getRegisteredCourse(): ResultData<List<Course>> {
        return ResultData.Success(courseDao.getRegisteredCourseList())
    }

    override suspend fun setFavoriteCourse(courseId: String, isFavorite: Boolean): ResultData<*> {
        return try {
            ResultData.Success(courseDao.setFavoriteCourse(courseId, isFavorite))
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun getFavoriteCourseList(): ResultData<List<Course>> {
        return ResultData.Success(courseDao.getFavoriteCourseList())
    }
}
