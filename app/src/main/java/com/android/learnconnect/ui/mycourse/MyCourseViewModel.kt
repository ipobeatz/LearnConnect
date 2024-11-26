package com.android.learnconnect.ui.mycourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.usecase.GetRegisteredCourseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCourseViewModel @Inject constructor(
    private val getRegisteredCourseListUseCase: GetRegisteredCourseListUseCase
) : ViewModel() {

    private val _registeredCourses = MutableStateFlow<ResultData<List<Course>>>(ResultData.Loading())
    val registeredCourses: StateFlow<ResultData<List<Course>>> get() = _registeredCourses

    fun fetchRegisteredCourses() {
        viewModelScope.launch {
            getRegisteredCourseListUseCase().collect { result ->
                _registeredCourses.value = result
            }
        }
    }
}
