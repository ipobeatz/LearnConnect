package com.android.learnconnect.ui.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.DashboardItem
import com.bumptech.glide.RequestManager

const val VIEW_TYPE_COURSE_LIST = 0
const val VIEW_TYPE_COURSE_ROW = 1

class MainAdapter(
    private val items: List<DashboardItem>,
    private val glide: RequestManager,
    private val listener: OnDashboardItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var courseIdHashMapFavorite: HashMap<String, Boolean> = hashMapOf()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DashboardItem.HorizontalList -> VIEW_TYPE_COURSE_LIST
            is DashboardItem.CourseRow -> VIEW_TYPE_COURSE_ROW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_COURSE_LIST -> HorizontalListViewHolder(
                inflater.inflate(
                    R.layout.course_horizontal_list_item, parent, false
                ), listener, courseIdHashMapFavorite
            )

            VIEW_TYPE_COURSE_ROW -> {
                val viewHolder = CourseRowViewHolder(
                    inflater.inflate(
                        R.layout.filtered_item_course, parent, false
                    ), glide = glide, listener = listener
                )
                viewHolder.itemView.setOnClickListener {
                    listener.onItemClicked((items[viewHolder.bindingAdapterPosition] as DashboardItem.CourseRow).course)
                }
                viewHolder
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HorizontalListViewHolder -> holder.bind(items[position] as DashboardItem.HorizontalList)
            is CourseRowViewHolder -> holder.bind(items[position] as DashboardItem.CourseRow)
        }
    }

    override fun getItemCount(): Int = items.size

    class HorizontalListViewHolder(
        itemView: View,
        private val listener: OnDashboardItemClickListener,
        private val courseIdHashMapFavorite: HashMap<String, Boolean>
    ) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView =
            itemView.findViewById(R.id.horizontalCourseRecyclerView)

        private val titleTextView: TextView =
            itemView.findViewById(R.id.courseTitleText)

        fun bind(item: DashboardItem.HorizontalList) {
            val adapter = HorizontalCourseAdapter(item.items, listener)
            titleTextView.text = "En Popüler " + item.items.firstOrNull()?.category + " Kursları"
            adapter.setHashmap(courseIdHashMap = courseIdHashMapFavorite)
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    class CourseRowViewHolder(
        itemView: View, val listener: OnDashboardItemClickListener, val glide: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {
        private val courseImage: ImageView = itemView.findViewById(R.id.courseImage)
        private val courseName: TextView = itemView.findViewById(R.id.coursesName)
        private val courseDescription: TextView = itemView.findViewById(R.id.coursesDescription)
        private val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)

        fun bind(item: DashboardItem.CourseRow) {
            val course: Course = item.course
            courseName.text = course.name
            courseDescription.text = course.description
            coursePrice.text = "$${course.coursePrice}"

            glide.load(course.imageUrl).placeholder(R.drawable.studio) // Yer tutucu resim
                .into(courseImage)

            itemView.setOnClickListener {
                listener.onItemClicked(course)
            }
        }
    }

    fun setHashmap(courseIdHashMap: HashMap<String, Boolean>) {
        courseIdHashMapFavorite.clear()
        courseIdHashMapFavorite = courseIdHashMap
    }
}
