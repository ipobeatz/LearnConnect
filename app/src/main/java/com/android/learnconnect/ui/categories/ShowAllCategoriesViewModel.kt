package com.android.learnconnect.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.usecase.GetCategoryListUseCase
import com.android.learnconnect.domain.usecase.GetCourseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class ShowAllCategoriesViewModel @Inject constructor(
    private val getCourseListUseCase: GetCourseListUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {

    private val _courseListData: MutableStateFlow<ResultData<List<Course>>> =
        MutableStateFlow(ResultData.Loading())

    @VisibleForTesting
    internal val courseList: StateFlow<ResultData<List<Course>>> = _courseListData

    private val _categoryListData: MutableStateFlow<ResultData<List<Category>>> =
        MutableStateFlow(ResultData.Loading())

    @VisibleForTesting
    internal val categoryList: StateFlow<ResultData<List<Category>>> = _categoryListData

    fun getCourseListData() {
        viewModelScope.launch {
            getCourseListUseCase().collect {
                _courseListData.value = it
            }
        }
    }

    fun getCategoryListData() {
        viewModelScope.launch {
            getCategoryListUseCase().collect {
                _categoryListData.value = it
            }
        }
    }
}
