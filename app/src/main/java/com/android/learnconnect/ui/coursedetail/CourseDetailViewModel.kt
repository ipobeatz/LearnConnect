package com.android.learnconnect.ui.coursedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.learnconnect.domain.entity.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor() : ViewModel() {

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> get() = _isRegistered

    private var currentCourse: Course? = null

    fun setCourseDetails(courseName: String, isRegistered: Boolean) {
        _isRegistered.value = isRegistered
        currentCourse = Course(
            id = "0", // ID gerekli değil, sadece state yönetimi için
            name = courseName,
            description = "",
            imageUrl = "",
            isRegistered = isRegistered,
            videos = emptyList(),
            rating = 0f,
            coursePrice = 0f,
            category = ""
        )
    }

    fun registerToCourse() {
        currentCourse?.let {
            it.isRegistered = true
            _isRegistered.value = true
            // Eğer backend varsa buraya API çağrısı koyabilirsiniz
        }
    }
}
