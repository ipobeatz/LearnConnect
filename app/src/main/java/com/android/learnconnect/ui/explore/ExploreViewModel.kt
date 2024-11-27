package com.android.learnconnect.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.learnconnect.domain.usecase.GetCategoryListUseCase
import com.android.learnconnect.domain.usecase.GetCourseListUseCase
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.usecase.FilterCourseByCategoryNameUseCase
import com.android.learnconnect.domain.usecase.SetFavoriteCourseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val getCourseListUseCase: GetCourseListUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val filterCourseByCategoryNameUseCase: FilterCourseByCategoryNameUseCase,
) : ViewModel() {

    private val _courseListData: MutableStateFlow<ResultData<List<Course>>> =
        MutableStateFlow(ResultData.Loading())
    @VisibleForTesting
    internal val courseList: StateFlow<ResultData<List<Course>>> = _courseListData

    private val _filteredCourseListData: MutableStateFlow<ResultData<List<Course>>> =
        MutableStateFlow(ResultData.Loading())
    @VisibleForTesting
    internal val filteredCourseListData: StateFlow<ResultData<List<Course>>> = _filteredCourseListData

    private val _filteredSecondCourseListData: MutableStateFlow<ResultData<List<Course>>> =
        MutableStateFlow(ResultData.Loading())
    @VisibleForTesting
    internal val filteredSecondCourseListData: StateFlow<ResultData<List<Course>>> = _filteredSecondCourseListData

    private val _categoryListData: MutableStateFlow<ResultData<List<Category>>> =
        MutableStateFlow(ResultData.Loading())
    @VisibleForTesting
    internal val categoryList: StateFlow<ResultData<List<Category>>> = _categoryListData

    fun getCourseListData() {
        _courseListData.value = ResultData.Loading()
        viewModelScope.launch {
            getCourseListUseCase().collect {
                _courseListData.value = it
            }
        }
    }

    fun getCategoryListData() {
        _categoryListData.value = ResultData.Loading()
        viewModelScope.launch {
            getCategoryListUseCase().collect {
                _categoryListData.value = it
            }
        }
    }

    fun getCourseDataFromCategory(category: String) {
        _filteredCourseListData.value = ResultData.Loading()
        viewModelScope.launch {
            filterCourseByCategoryNameUseCase(category).collect {
                _filteredCourseListData.value = it
            }
        }
    }

    fun getCourseSecondDataFromCategory(category: String) {
        _filteredSecondCourseListData.value = ResultData.Loading()
        viewModelScope.launch {
            filterCourseByCategoryNameUseCase(category).collect {
                _filteredSecondCourseListData.value = it
            }
        }
    }
}
