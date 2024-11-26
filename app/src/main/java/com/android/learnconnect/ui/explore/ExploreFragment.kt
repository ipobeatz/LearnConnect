package com.android.learnconnect.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.MainActivity
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Category
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.LearnConnectConstants.FILTERED_COURSE_DATA_LIST
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment @Inject constructor() : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private var courseListData: List<Course> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCourseListData()
        viewModel.getCourseDataFromCategory("Yazılım")
        viewModel.getCategoryListData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseList.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            setupCourseUI(it.data)
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
                viewModel.filteredCourseListData.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            setupCourseUI2(it.data)
                            (requireActivity() as MainActivity).hideLoading()
                        }

                        is ResultData.Error -> {
                            (requireActivity() as MainActivity).hideLoading()
                        }

                        is ResultData.Loading -> {
                            (requireActivity() as MainActivity).showLoading()
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

    private fun setupCourseUI(courses: List<Course>) {
        courseListData = courses
        val coursesRecyclerView = requireView().findViewById<RecyclerView>(R.id.popularCoursesRecyclerView)
        coursesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        coursesRecyclerView.adapter = CoursesAdapter(courses) { course ->
            val action = ExploreFragmentDirections.actionExploreFragmentToCourseDetailFragment(
                courseId = course.id
            )
            findNavController().navigate(action)
        }
    }

    private fun setupCourseUI2(courses: List<Course>) {
        val coursesRecyclerView = requireView().findViewById<RecyclerView>(R.id.popularCoursesRecyclerView2)
        coursesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        coursesRecyclerView.adapter = CoursesAdapter(courses) { course ->
            val action = ExploreFragmentDirections.actionExploreFragmentToCourseDetailFragment(
                courseId = course.id
            )
            findNavController().navigate(action)
        }
    }

    private fun setupCategoryUI(categories: List<Category>) {
        val categoriesRecyclerView = requireView().findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        categoriesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.adapter = CategoriesAdapter(categories) { category ->
            Toast.makeText(requireContext(), "Selected: ${category.title}", Toast.LENGTH_SHORT)
                .show()
        }

        // Tüm Kategoriler Buttonu
        val showAllButton = requireView().findViewById<TextView>(R.id.showAllCategories)
        showAllButton.setOnClickListener {
            findNavController().navigate(R.id.action_exploreFragment_to_showAllCategoriesFragment)
        }

        categoriesRecyclerView.adapter = CategoriesAdapter(categories) { category ->
            val filteredCourses = courseListData.filter { it.category == category.title }

            val bundle = Bundle().apply {
                putString("categoryTitle", category.title)
                putSerializable(FILTERED_COURSE_DATA_LIST, filteredCourses as ArrayList<Course>)
            }
            findNavController().navigate(R.id.action_exploreFragment_to_filteredCoursesFragment, bundle)
        }
    }
}
