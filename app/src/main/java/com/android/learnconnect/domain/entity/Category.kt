package com.android.learnconnect.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val title: String,
    val description: String
)