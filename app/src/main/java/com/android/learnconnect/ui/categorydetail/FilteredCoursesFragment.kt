package com.android.learnconnect.ui.categorydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.mockdata.LearnConnectConstants.FILTERED_COURSE_DATA_LIST
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilteredCoursesFragment @Inject constructor() : Fragment() {

    private val viewModel: FilteredCoursesViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtered_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courses = arguments?.getSerializable(FILTERED_COURSE_DATA_LIST) as? ArrayList<Course>

        // Toolbar'ı bul ve başlık ayarla
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.title = arguments?.getString("categoryTitle") ?: "Kategoriler"
        toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // RecyclerView için ayarlar
        val recyclerView = view.findViewById<RecyclerView>(R.id.filteredCoursesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = courses?.let {
            FilteredCoursesAdapter(it, glide = glide) { course ->
                val action = FilteredCoursesFragmentDirections
                    .actionFilteredCoursesFragmentToCourseDetailFragment(
                        videoId = course.id
                    )
                findNavController().navigate(action)
            }
        }
    }
}
