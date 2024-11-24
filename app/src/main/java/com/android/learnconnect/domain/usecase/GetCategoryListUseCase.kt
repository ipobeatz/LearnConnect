package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.LearnConnectRepository
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val repository: LearnConnectRepository
) {
    suspend operator fun invoke(): Flow<ResultData<List<Category>>> {
        return repository.getCategoryList()
    }
}
