package com.android.learnconnect.exploreScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.learnconnect.R
import com.bumptech.glide.Glide

import androidx.navigation.fragment.navArgs

class CourseDetailFragment : Fragment() {

    private val args: CourseDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseName = args.courseName
        val courseDescription = args.courseDescription
        val courseImageUrl = args.courseImageUrl
        val coursePrice = args.coursePrice

        val courseNameTextView: TextView = view.findViewById(R.id.detailCourseName)
        val courseDescriptionTextView: TextView = view.findViewById(R.id.detailCourseDescription)
        val courseImageView: ImageView = view.findViewById(R.id.detailCourseImage)
        val coursePriceTextView: TextView = view.findViewById(R.id.detailCoursePrice)

        courseNameTextView.text = courseName
        courseDescriptionTextView.text = courseDescription
        coursePriceTextView.text = coursePrice
        Glide.with(this)
            .load(courseImageUrl)
            .placeholder(R.drawable.studio)
            .into(courseImageView)
    }
}
