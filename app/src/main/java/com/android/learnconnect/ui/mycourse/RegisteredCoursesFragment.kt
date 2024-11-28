package com.android.learnconnect.ui.mycourse

import android.content.Context
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
import com.android.learnconnect.databinding.FragmentRegisteredCoursesBinding
import com.android.learnconnect.domain.entity.ResultData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisteredCoursesFragment : Fragment() {
    private var _binding: FragmentRegisteredCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyCourseViewModel by viewModels()
    private var navigationListener: CourseNavigationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CourseNavigationListener) {
            navigationListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisteredCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CourseAdapter { course ->
            navigateToCourseDetail(course.id)
        }
        binding.recyclerid.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerid.adapter = adapter

        viewModel.fetchRegisteredCourses()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registeredCourses.collectLatest { result ->
                    when (result) {
                        is ResultData.Loading -> {
                        }

                        is ResultData.Success -> {
                            if (result.data.isEmpty()) {
                                binding.recyclerid.visibility = View.GONE
                                binding.emptyView.visibility = View.VISIBLE
                            } else {
                                binding.recyclerid.visibility = View.VISIBLE
                                binding.emptyView.visibility = View.GONE
                                adapter.submitList(result.data)
                            }
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
