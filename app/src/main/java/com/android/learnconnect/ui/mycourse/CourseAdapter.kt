package com.android.learnconnect.ui.mycourse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FilteredItemCourseBinding
import com.android.learnconnect.domain.entity.Course
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class CourseAdapter(
    private val onCourseClick: (Course) -> Unit
) : ListAdapter<Course, CourseAdapter.CourseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding =
            FilteredItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
        holder.itemView.setOnClickListener {
            onCourseClick(course)
        }
    }

    class CourseViewHolder(private val binding: FilteredItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.coursesName.text = course.name
            binding.coursesDescription.text = course.description
            binding.courseRating.text = course.rating.toString()
            binding.coursePrice.text =
                NumberFormat.getCurrencyInstance(Locale.US).format(course.coursePrice)

            Glide.with(binding.root.context).load(course.imageUrl).placeholder(R.drawable.studio)
                .into(binding.courseImage)

            val stars = listOf(
                binding.star1, binding.star2, binding.star3, binding.star4, binding.star5
            )
            setStarRating(course.rating, stars)
        }

        private fun setStarRating(rating: Float, stars: List<ImageView>) {
            for (i in stars.indices) {
                when {
                    i < rating.toInt() -> stars[i].setImageResource(R.drawable.fullstar)
                    i < rating -> stars[i].setImageResource(R.drawable.halfstar)
                    else -> stars[i].setImageResource(android.R.drawable.btn_star)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}
