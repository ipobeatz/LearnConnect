package com.android.learnconnect.ui.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.learnconnect.data.category.CategoryRepository

class FilteredCoursesViewModelFactory(
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilteredCoursesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilteredCoursesViewModel(categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
