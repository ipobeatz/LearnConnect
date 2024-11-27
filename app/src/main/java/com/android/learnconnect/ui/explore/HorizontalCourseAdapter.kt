package com.android.learnconnect.ui.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Course
import com.bumptech.glide.Glide

class HorizontalCourseAdapter(
    private val courses: List<Course>,
    private val listener: OnDashboardItemClickListener
) : RecyclerView.Adapter<HorizontalCourseAdapter.CourseViewHolder>() {

    private var courseIdHashMapFavorite: HashMap<String, Boolean> = hashMapOf()

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseImage: ImageView = itemView.findViewById(R.id.courseImage)
        private val courseName: TextView = itemView.findViewById(R.id.coursesName)
        private val courseDescription: TextView = itemView.findViewById(R.id.coursesDescription)
        private val courseRating: TextView = itemView.findViewById(R.id.courseRating)
        private val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favoriteButton)

        fun bind(course: Course) {
            courseName.text = course.name
            courseDescription.text = course.description
            courseRating.text = course.rating.toString()
            coursePrice.text = "$${course.coursePrice}"

            // Glide ile görseli yükle
            Glide.with(itemView.context)
                .load(course.imageUrl)
                .placeholder(R.drawable.studio)
                .into(courseImage)

            println("mcmc --> " + course.name + " -- " + courseIdHashMapFavorite[course.id])
            // Favoriler butonuna tıklama işlemi
            favoriteButton.setImageResource(
                if (courseIdHashMapFavorite[course.id] == true) R.drawable.love_svgrepo_com else R.drawable.love_svgrepo_com_2
            )

            // Glide ile resmi yükle
            Glide.with(itemView.context)
                .load(course.imageUrl)
                .placeholder(R.drawable.studio)
                .into(courseImage)

            val star1: ImageView = itemView.findViewById(R.id.star1)
            val star2: ImageView = itemView.findViewById(R.id.star2)
            val star3: ImageView = itemView.findViewById(R.id.star3)
            val star4: ImageView = itemView.findViewById(R.id.star4)
            val star5: ImageView = itemView.findViewById(R.id.star5)


            // Yıldız doluluğunu ayarla
            val stars = listOf(star1, star2, star3, star4, star5)
            val fullStars = course.rating.toInt() // Tam dolu yıldız sayısı
            val halfStar = course.rating % 1 >= 0.5 // Yarım yıldız durumu

            for (i in stars.indices) {
                when {
                    i < fullStars -> stars[i].setImageResource(R.drawable.fullstar) // Tam dolu yıldız
                    i == fullStars && halfStar -> stars[i].setImageResource(R.drawable.halfstar) // Yarım yıldız
                    else -> stars[i].setImageResource(android.R.drawable.btn_star_big_off) // Boş yıldız
                }
            }

            // Tıklama işlemi
            itemView.setOnClickListener {
                listener.onItemClicked(course)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    fun setHashmap(courseIdHashMap: HashMap<String, Boolean>) {
        courseIdHashMapFavorite.clear()
        courseIdHashMapFavorite = courseIdHashMap
    }

    override fun getItemCount(): Int = courses.size
}
