package com.android.learnconnect.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val id: String,
    val title: String,
    val duration: String,
    var lastSecond: Int,
    val videoUrl: String
)
