package com.android.learnconnect.ui.mycourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.learnconnect.databinding.FragmentFavoritesBinding
import com.android.learnconnect.domain.entity.ResultData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private val viewModel: MyCourseViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var navigationListener: CourseNavigationListener? = null

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (parentFragment is CourseNavigationListener) {
            navigationListener = parentFragment as CourseNavigationListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CourseAdapter { course ->
            navigateToCourseDetail(course.id)
        }
        binding.favoriteCoursesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteCoursesRecyclerView.adapter = adapter

        viewModel.fetchFavoriteCourses()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteCourses.collectLatest { result ->
                    when (result) {
                        is ResultData.Loading -> {
                        }

                        is ResultData.Success -> {
                            println("mcmc favorite is -> ${result.data}")
                            adapter.submitList(result.data)
                        }


                        is ResultData.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${result.exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToCourseDetail(courseId: String) {
        navigationListener?.onNavigateToCourseDetail(courseId)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

