package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.CourseRepository
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRegisteredCourseListUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): Flow<ResultData<List<Course>>> = flow {
        emit(ResultData.Loading()) // Verinin yüklenme durumunu göster
        try {
            val courses = repository.getRegisteredCourse() // Parametre olmadan çağır
            emit(repository.getRegisteredCourse())
        } catch (e: Exception) {
            emit(ResultData.Error(e)) // Hata durumunu işleyin
        }
    }
}