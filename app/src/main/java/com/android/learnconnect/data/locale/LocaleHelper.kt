package com.android.learnconnect.data.locale

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = when (language) {
            "English" -> Locale.ENGLISH
            "Türkçe" -> Locale("tr")
            else -> Locale.getDefault()
        }

        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
