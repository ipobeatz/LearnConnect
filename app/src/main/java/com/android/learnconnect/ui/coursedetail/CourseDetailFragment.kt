package com.android.learnconnect.ui.coursedetail

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentCourseDetailBinding
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CourseDetailFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseDetailViewModel by viewModels()
    private val args: CourseDetailFragmentArgs by navArgs()
    private var isRegistered = false
    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val courseId = args.courseId
        viewModel.getCourseDataFromId(courseId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseData.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            setupUI(it.data)
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

    private fun setupUI(course: Course) {
        isRegistered = course.isRegistered
        binding.apply {
            detailCourseName.text = course.name
            detailCourseDescription.text = course.description
            detailCoursePrice.text = course.coursePrice.toString()
            glide.load(course.imageUrl).placeholder(R.drawable.studio).into(detailCourseImage)
            if (isRegistered) {
                enrollButton.text = "Kursa Git"
            } else {
                enrollButton.text = "Kursa Kaydolun"
            }
            enrollButton.setOnClickListener {
                if (isRegistered) {
                    findNavController().navigate(R.id.action_courseDetailFragment_to_courseContentFragment)
                } else {
                    viewModel.registerToCourse(courseId = course.id)
                    isRegistered = true
                    binding.enrollButton.text = "Kursa git"
                    Toast.makeText(requireContext(), "Kursa başarıyla kaydoldunuz!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
