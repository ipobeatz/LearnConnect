package com.android.learnconnect.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val isRegistered: Boolean,
    val videos: List<String>,
    val rating: Float,
    val coursePrice : Float,
    val category: String
)
