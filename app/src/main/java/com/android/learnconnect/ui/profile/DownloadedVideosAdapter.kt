package com.android.learnconnect.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R

class DownloadedVideosAdapter(
    private val videoList: List<String>, private val onVideoClick: (String) -> Unit
) : RecyclerView.Adapter<DownloadedVideosAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoTitle = itemView.findViewById<TextView>(R.id.coursesName)
        fun bind(videoPath: String) {
            val fileName = videoPath.substringAfterLast("/")
            videoTitle.text = fileName
            itemView.setOnClickListener { onVideoClick(videoPath) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filtered_item_course, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size
}
