package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.course.CourseRepository
import javax.inject.Inject

class SetFavoriteCourseUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: String, isFavorite: Boolean) {
        repository.setFavoriteCourse(courseId, isFavorite)
    }
}
