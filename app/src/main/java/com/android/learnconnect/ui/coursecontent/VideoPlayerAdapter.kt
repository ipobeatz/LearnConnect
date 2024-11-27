package com.android.learnconnect.ui.coursecontent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.domain.entity.VideoItem

class VideoPlayerAdapter(
    private val videoList: List<VideoItem>,
    private val onItemClick: (VideoItem) -> Unit,
    private val onDownloadClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<VideoPlayerAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.bind(video)
        holder.itemView.setOnClickListener {
            onItemClick(video)
        }
        holder.downloadButton.setOnClickListener {
            onDownloadClick(video)
        }
    }

    override fun getItemCount(): Int = videoList.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.videoTitle)
        private val durationTextView: TextView = itemView.findViewById(R.id.videoDuration)
        val downloadButton: ImageView = itemView.findViewById(R.id.downloadButton)

        fun bind(video: VideoItem) {
            titleTextView.text = video.title
            durationTextView.text = formatSeconds(video.lastSecond) + "/" + video.duration
        }

        private fun formatSeconds(seconds: Int): String {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%d:%02d", minutes, remainingSeconds)
        }
    }
}
