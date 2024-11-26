package com.android.learnconnect.ui.coursedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.usecase.GetCourseByIdUseCase
import com.android.learnconnect.domain.usecase.RegisterCourseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(private val getCourseByIdUseCase: GetCourseByIdUseCase, private val registerCourseUseCase: RegisterCourseUseCase) :
    ViewModel() {

    private val _isRegisteredSuccessfully = MutableLiveData<ResultData<*>>()
    val isRegisteredSuccessfully: LiveData<ResultData<*>> get() = _isRegisteredSuccessfully

    private val _courseData: MutableStateFlow<ResultData<Course>> =
        MutableStateFlow(ResultData.Loading())
    @VisibleForTesting
    internal val courseData: StateFlow<ResultData<Course>> = _courseData

    fun registerToCourse(courseId: String) {
        viewModelScope.launch {
            registerCourseUseCase.invoke(courseId = courseId)
        }
    }

    fun getCourseDataFromId(courseId: String) {
        viewModelScope.launch {
            getCourseByIdUseCase.invoke(courseId = courseId).collectLatest {
                _courseData.value = it
            }
        }
    }
}
