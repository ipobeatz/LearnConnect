package com.android.learnconnect.data

import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class LearnConnectDataSource @Inject constructor(
    private val networkJson: Json,
    private val assets: FakeAssetManager,
) : LearnConnectRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getAllCourseList(): Flow<ResultData<List<Course>>> = flow {
        try {
            val courses = assets.open(COURSES_JSON).use { inputStream ->
                networkJson.decodeFromStream<List<Course>>(inputStream)
            }
            emit(ResultData.Success(courses))
        } catch (e: Exception) {
            emit(ResultData.Error(Exception("Error during parse json: ${e.message}")))
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getCategoryList(): Flow<ResultData<List<Category>>> = flow {
        try {
            val courses = assets.open(CATEGORIES_JSON).use { inputStream ->
                networkJson.decodeFromStream<List<Category>>(inputStream)
            }
            emit(ResultData.Success(courses))
        } catch (e: Exception) {
            emit(ResultData.Error(Exception("Error during parse json: ${e.message}")))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getLocalRegisteredList(): List<Course> {
        return listOf()
    }

    companion object {
        private const val CATEGORIES_JSON = "categories.json"
        private const val COURSES_JSON = "courses.json"
    }
}
