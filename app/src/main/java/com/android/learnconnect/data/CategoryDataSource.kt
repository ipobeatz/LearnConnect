package com.android.learnconnect.data

import com.android.learnconnect.domain.entity.Category
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

class CategoryDataSource @Inject constructor(
    private val networkJson: Json, private val assets: FakeAssetManager
) : CategoryRepository {

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

    companion object {
        private const val CATEGORIES_JSON = "categories.json"
    }
}
