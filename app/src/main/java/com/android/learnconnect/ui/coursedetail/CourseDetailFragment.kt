package com.android.learnconnect.ui.coursedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentCourseDetailBinding
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CourseDetailFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseDetailViewModel by viewModels()
    private val args: CourseDetailFragmentArgs by navArgs()

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

        val courseName = args.courseName
        val courseDescription = args.courseDescription
        val courseImageUrl = args.courseImageUrl
        val coursePrice = args.coursePrice
        val isRegistered = args.isRegistered

        binding.apply {
            detailCourseName.text = courseName
            detailCourseDescription.text = courseDescription
            detailCoursePrice.text = coursePrice
            glide.load(courseImageUrl).placeholder(R.drawable.studio).into(detailCourseImage)

            // ViewModel'deki kurs verisini güncelle
            viewModel.setCourseDetails(courseName, isRegistered)

            // Butonu gözlemle ve güncelle
            viewModel.isRegistered.observe(viewLifecycleOwner) { registered ->
                if (registered) {
                    enrollButton.text = "Kursa Git"
                    enrollButton.setOnClickListener {
                        findNavController().navigate(R.id.action_courseDetailFragment_to_courseContentFragment)
                    }
                } else {
                    enrollButton.text = "Kursa Kaydolun"
                    enrollButton.setOnClickListener {
                        viewModel.registerToCourse()
                        Toast.makeText(requireContext(), "Kursa başarıyla kaydoldunuz!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
