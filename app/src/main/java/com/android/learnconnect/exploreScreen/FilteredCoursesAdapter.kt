package com.android.learnconnect.exploreScreen


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.exploreScreen.models.Course
import com.bumptech.glide.Glide

class FilteredCoursesAdapter(
    private val courses: List<Course>,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<FilteredCoursesAdapter.FilteredCourseViewHolder>() {

    inner class FilteredCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseImage: ImageView = itemView.findViewById(R.id.courseImage)
        private val courseName: TextView = itemView.findViewById(R.id.courseName)
        private val courseDescription: TextView = itemView.findViewById(R.id.courseDescription)
        private val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)

        fun bind(course: Course) {
            courseName.text = course.name
            courseDescription.text = course.description
            coursePrice.text = "$${course.coursePrice}"

            // Glide ile resmi yükle
            Glide.with(itemView.context)
                .load(course.imageUrl)
                .placeholder(R.drawable.studio) // Yer tutucu resim
                .into(courseImage)

            // Tıklama işlemi
            itemView.setOnClickListener {
                onItemClick(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredCourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filtered_item_course, parent, false)
        return FilteredCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilteredCourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size
}
