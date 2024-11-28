package com.android.learnconnect.ui.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.learnconnect.data.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class FilteredCoursesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    fun getCategories() = liveData(Dispatchers.IO) {
        // getCategoryList suspend fonksiyonunu coroutine içinde çağır
        val categories = categoryRepository.getCategoryList().first() // Flow'dan ilk değeri al
        emit(categories) // LiveData'ya sonucu yolla
    }
}
