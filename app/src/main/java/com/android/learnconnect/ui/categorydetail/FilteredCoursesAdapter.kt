package com.android.learnconnect.ui.categorydetail


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Course
import com.bumptech.glide.RequestManager

class FilteredCoursesAdapter(
    private val courses: List<Course>,
    private val glide: RequestManager,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<FilteredCoursesAdapter.FilteredCourseViewHolder>() {

    inner class FilteredCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseImage: ImageView = itemView.findViewById(R.id.courseImage)
        private val courseName: TextView = itemView.findViewById(R.id.coursesName)
        private val courseDescription: TextView = itemView.findViewById(R.id.coursesDescription)
        private val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)

        fun bind(course: Course) {
            courseName.text = course.name
            courseDescription.text = course.description
            coursePrice.text = "$${course.coursePrice}"

            glide.load(course.imageUrl)
                .placeholder(R.drawable.studio) // Yer tutucu resim
                .into(courseImage)

            itemView.setOnClickListener {
                onItemClick(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredCourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filtered_item_course, parent, false)
        return FilteredCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilteredCourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}
