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
import com.android.learnconnect.domain.entity.DashboardItem
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.LearnConnectConstants.FILTERED_COURSE_DATA_LIST
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment @Inject constructor() : Fragment(), OnDashboardItemClickListener {

    private val viewModel: ExploreViewModel by viewModels()
    private var courseListData: List<Course> = listOf()
    private val courseIdHashMapFavorite: HashMap<String, Boolean> = hashMapOf()
    private var courseAdapter: MainAdapter? = null
    private val exploreItemList: ArrayList<DashboardItem> = arrayListOf()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCourseListData()
        viewModel.getCourseDataFromCategory("Yazılım")
        viewModel.getCourseSecondDataFromCategory("Kişisel Gelişim")
        viewModel.getCategoryListData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseList.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            it.data.forEach { course ->
                                exploreItemList.add(DashboardItem.CourseRow(course))
                            }

                            courseListData = it.data
                            it.data.forEach {
                                courseIdHashMapFavorite.set(it.id, it.isFavorite)
                            }
                            setupCourseUI()
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
                            exploreItemList.add(DashboardItem.HorizontalList(it.data))
                            (requireActivity() as MainActivity).hideLoading()
                        }

                        is ResultData.Error -> {
                            (requireActivity() as MainActivity).hideLoading()
                        }

                        is ResultData.Loading -> {
                            //(requireActivity() as MainActivity).showLoading()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.courseList,
                    viewModel.filteredCourseListData,
                    viewModel.filteredSecondCourseListData
                ) { courseList, filteredList, filteredSecondData ->
                    // Her iki Flow'dan gelen veriyi birleştir
                    Triple(courseList, filteredList, filteredSecondData)
                }.collectLatest { (courseList, filteredList, filteredSecondData) ->
                    when {
                        courseList is ResultData.Success && filteredList is ResultData.Success && filteredSecondData is ResultData.Success -> {
                            exploreItemList.clear()
                            exploreItemList.add(DashboardItem.HorizontalList(filteredList.data))
                            exploreItemList.add(DashboardItem.HorizontalList(filteredSecondData.data))
                            courseList.data.forEach {
                                exploreItemList.add(DashboardItem.CourseRow(it))
                                courseIdHashMapFavorite[it.id] = it.isFavorite
                            }
                            (requireActivity() as MainActivity).hideLoading()
                            setupCourseUI()
                        }

                        courseList is ResultData.Loading || filteredList is ResultData.Loading -> {
                            (requireActivity() as MainActivity).showLoading()
                        }

                        courseList is ResultData.Error || filteredList is ResultData.Error -> {
                            (requireActivity() as MainActivity).hideLoading()
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

    private fun setupCourseUI() {
        val coursesRecyclerView = requireView().findViewById<RecyclerView>(R.id.exploreRecyclerView)
        coursesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        courseAdapter = MainAdapter(
            exploreItemList, glide = glide, listener = this
        )
        courseAdapter?.setHashmap(courseIdHashMapFavorite)
        coursesRecyclerView.adapter = courseAdapter
    }

    /*private fun setupCourseUI2(courses: List<Course>) {
        if (courses.isNotEmpty()) {
            val coursesRecyclerView = requireView().findViewById<RecyclerView>(R.id.popularCoursesRecyclerView2)
            coursesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            courseAdapter = CoursesAdapter(
                courses,
                onItemClick = { course ->
                    val action = ExploreFragmentDirections.actionExploreFragmentToCourseDetailFragment(
                        courseId = course.id
                    )
                    findNavController().navigate(action)
                },
                onFavoriteClick = { course ->
                    val newStatus = courseIdHashMapFavorite.get(course.id)
                    viewModel.setCourseFavorite(course.id, newStatus ?: false)
                    val message = if (newStatus == true) {
                        "${course.name} favorilere eklendi."
                    } else {
                        "${course.name} favorilerden kaldırıldı."
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            )
            courseAdapter?.setHashmap(courseIdHashMapFavorite)
            coursesRecyclerView.adapter = courseAdapter
        } else {
            // Eğer liste boşsa yapılacaklar
            Toast.makeText(requireContext(), "Kurs listesi boş", Toast.LENGTH_SHORT).show()
        }
    }

     */

    private fun setupCategoryUI(categories: List<Category>) {
        val categoriesRecyclerView =
            requireView().findViewById<RecyclerView>(R.id.categoriesRecyclerView)
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
            findNavController().navigate(
                R.id.action_exploreFragment_to_filteredCoursesFragment, bundle
            )
        }
    }

    override fun onItemClicked(course: Course) {
        val action = ExploreFragmentDirections.actionExploreFragmentToCourseDetailFragment(
            videoId = course.id
        )
        findNavController().navigate(action)
    }
}
