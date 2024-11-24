package com.android.learnconnect.exploreScreen.models

import android.provider.MediaStore

data class Course(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val isRegistered: Boolean,
    val videos: List<MediaStore.Video>,
    val rating: Float,
    val coursePrice : Float,
    val category: String
)
