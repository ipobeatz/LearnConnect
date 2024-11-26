package com.android.learnconnect.ui.mycourse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Course
import org.w3c.dom.Text

class CourseAdapter : ListAdapter<Course, CourseAdapter.CourseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filtered_item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseImage = itemView.findViewById<ImageView>(R.id.courseImage) // Kurs görseli
        private val courseName = itemView.findViewById<TextView>(R.id.coursesName) // Kurs adı
        private val courseDescription = itemView.findViewById<TextView>(R.id.coursesDescription) // Kurs açıklaması
        private val courseRating = itemView.findViewById<TextView>(R.id.courseRating) // Rating text
        private val coursePrice = itemView.findViewById<TextView>(R.id.coursePrice) // Fiyat

        fun bind(course: Course) {
            // Mevcut alanların atanması
            courseName.text = course.name
            courseDescription.text = course.description
            courseRating.text = course.rating.toString()
            coursePrice.text = "$${course.coursePrice}"

            // Yıldızlar dinamik olarak ayarlanıyor
            val stars = listOf(
                itemView.findViewById<ImageView>(R.id.star1),
                itemView.findViewById<ImageView>(R.id.star2),
                itemView.findViewById<ImageView>(R.id.star3),
                itemView.findViewById<ImageView>(R.id.star4),
                itemView.findViewById<ImageView>(R.id.star5)
            )
            setStarRating(course.rating, stars)
        }
        private fun setStarRating(rating: Float, stars: List<ImageView>) {
            for (i in stars.indices) {
                if (i < rating.toInt()) {
                    stars[i].setImageResource(android.R.drawable.btn_star_big_on) // Dolmuş yıldız
                } else {
                    stars[i].setImageResource(android.R.drawable.btn_star_big_off) // Boş yıldız
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

