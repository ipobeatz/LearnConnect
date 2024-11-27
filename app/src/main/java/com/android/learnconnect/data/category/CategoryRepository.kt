package com.android.learnconnect.data.category

import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoryList(): Flow<ResultData<List<Category>>>
}
