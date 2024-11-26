package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.CourseRepository
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteCourseListUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): Flow<ResultData<List<Course>>> = flow {
        emit(repository.getFavoriteCourseList())
    }
}