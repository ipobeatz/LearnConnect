package com.android.learnconnect.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.learnconnect.data.locale.Converters
import kotlinx.serialization.Serializable

@Entity(tableName = "courses")
@Serializable
@TypeConverters(Converters::class)
data class Course(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    var isRegistered: Boolean = false,
    var isFavorite: Boolean = false,
    val rating: Float,
    val coursePrice : Float,
    val category: String,
    val videoItem: List<VideoItem>
)
