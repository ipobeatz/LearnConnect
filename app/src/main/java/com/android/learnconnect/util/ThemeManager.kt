package com.android.learnconnect.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun applySavedTheme(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val selectedTheme = sharedPreferences.getInt("selected_theme", 2)

        val mode = when (selectedTheme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun saveTheme(context: Context, theme: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putInt("selected_theme", theme)
            apply()
        }
    }
}
