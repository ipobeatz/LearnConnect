package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.CourseRepository
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterCourseByKeywordUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(keyword: String): Flow<ResultData<List<Course>>> = flow {
        emit(repository.filterCourseByCategory(keyword))
    }
}
