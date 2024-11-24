package com.android.learnconnect.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.LearnConnectConstants.FILTERED_COURSE_DATA_LIST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowAllCategoriesFragment @Inject constructor() : Fragment() {

    private val viewModel: ShowAllCategoriesViewModel by viewModels()
    private var courseList: List<Course> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_all_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.getCourseListData()
        viewModel.getCategoryListData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseList.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            courseList = it.data
                        }

                        is ResultData.Error -> {

                        }

                        is ResultData.Loading -> {

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryList.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            setupCategoryUI(it.data)
                        }

                        is ResultData.Error -> {

                        }

                        is ResultData.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupCategoryUI(data: List<Category>) {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.allCategoriesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CategoriesListAdapter(data) { category ->
            val filteredCourses = courseList.filter { it.category == category.title }

            val bundle = Bundle().apply {
                putString("categoryTitle", category.title)
                putSerializable(FILTERED_COURSE_DATA_LIST, filteredCourses as ArrayList<Course>)
            }
            findNavController().navigate(
                R.id.action_showAllCategoriesFragment_to_filteredCoursesFragment,
                bundle
            )
        }
    }
}
