package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.LearnConnectRepository
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCourseListUseCase @Inject constructor(
    private val repository: LearnConnectRepository
) {
    suspend operator fun invoke(): Flow<ResultData<List<Course>>> {
        return repository.getAllCourseList()
    }
}
