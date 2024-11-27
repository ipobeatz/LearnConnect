package com.android.learnconnect.ui.coursecontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.usecase.GetCourseByIdUseCase
import com.android.learnconnect.domain.usecase.RegisterCourseUseCase
import com.android.learnconnect.domain.usecase.SetFavoriteCourseUseCase
import com.android.learnconnect.domain.usecase.UpdateLastSecondOfVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class CourseContentViewModel @Inject constructor(
    private val updateLastSecondOfVideoUseCase: UpdateLastSecondOfVideoUseCase,
    private val getCourseByIdUseCase: GetCourseByIdUseCase
) : ViewModel() {

    private val _courseData: MutableStateFlow<ResultData<Course>> =
        MutableStateFlow(ResultData.Loading())

    @VisibleForTesting
    internal val courseData: StateFlow<ResultData<Course>> =
        _courseData

    fun updateLastSecond(courseId: String, videoId: String, lastSecond: Int) {
        viewModelScope.launch {
            updateLastSecondOfVideoUseCase.invoke(courseId = courseId, videoId, lastSecond)
        }
    }

    fun getCourseData(courseId: String) {
        viewModelScope.launch {
            getCourseByIdUseCase.invoke(courseId = courseId).collectLatest {
                _courseData.value = it
            }
        }
    }
}
