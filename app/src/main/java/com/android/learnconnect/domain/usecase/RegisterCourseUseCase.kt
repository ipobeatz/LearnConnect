package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.course.CourseRepository
import com.android.learnconnect.domain.entity.ResultData
import javax.inject.Inject

class RegisterCourseUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId:String): ResultData<*> {
        return repository.registerCourse(courseId)
    }
}
