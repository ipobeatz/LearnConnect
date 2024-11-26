package com.android.learnconnect.data.locale

import androidx.room.TypeConverter
import com.android.learnconnect.domain.entity.VideoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromVideoItemList(value: List<VideoItem>): String {
        val type = object : TypeToken<List<VideoItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toVideoItemList(value: String): List<VideoItem> {
        val type = object : TypeToken<List<VideoItem>>() {}.type
        return gson.fromJson(value, type)
    }
}