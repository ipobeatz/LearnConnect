package com.android.learnconnect.ui.coursedetail

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
class CourseDetailViewModel @Inject constructor(
    private val setFavoriteCourseUseCase: SetFavoriteCourseUseCase,
    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val registerCourseUseCase: RegisterCourseUseCase,
    private val updateLastSecondOfVideoUseCase: UpdateLastSecondOfVideoUseCase,
) : ViewModel() {

    private val _isRegisteredSuccessfully = MutableLiveData<ResultData<*>>()
    val isRegisteredSuccessfully: LiveData<ResultData<*>> get() = _isRegisteredSuccessfully

    private val _filteredCourseListData: MutableStateFlow<ResultData<List<Course>>> =
        MutableStateFlow(ResultData.Loading())

    @VisibleForTesting
    internal val filteredCourseListData: StateFlow<ResultData<List<Course>>> =
        _filteredCourseListData


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

    fun setCourseFavorite(courseId: String, isFavorite: Boolean) {
        _filteredCourseListData.value = ResultData.Loading()
        viewModelScope.launch {
            setFavoriteCourseUseCase(courseId, isFavorite)
        }
    }

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
