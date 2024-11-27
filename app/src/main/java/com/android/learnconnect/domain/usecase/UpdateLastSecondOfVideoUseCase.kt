package com.android.learnconnect.domain.usecase

import com.android.learnconnect.data.course.CourseRepository
import javax.inject.Inject

class UpdateLastSecondOfVideoUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: String, videoId: String, lastSecond: Int) {
        repository.setLastSecondOfVide(courseId, videoId, lastSecond)
    }
}
