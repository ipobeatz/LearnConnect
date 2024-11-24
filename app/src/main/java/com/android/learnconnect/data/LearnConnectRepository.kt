package com.android.learnconnect.data

import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow

interface LearnConnectRepository {
    suspend fun getAllCourseList(): Flow<ResultData<List<Course>>>

    suspend fun getCategoryList(): Flow<ResultData<List<Category>>>

    suspend fun getLocalRegisteredList(): List<Course>
}
