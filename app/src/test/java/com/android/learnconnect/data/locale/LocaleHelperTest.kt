package com.android.learnconnect.data.locale

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class LocaleHelperTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun `setLocale changes locale to English`() {
        val modifiedContext = LocaleHelper.setLocale(context, "English")
        assertEquals(Locale.ENGLISH, Locale.getDefault())
        assertEquals(Locale.ENGLISH, modifiedContext.resources.configuration.locales.get(0))
    }

    @Test
    fun `setLocale changes locale to Turkish`() {
        val modifiedContext = LocaleHelper.setLocale(context, "Türkçe")
        assertEquals(Locale("tr"), Locale.getDefault())
        assertEquals(Locale("tr"), modifiedContext.resources.configuration.locales.get(0))
    }

    @Test
    fun `setLocale changes locale to default when language is unknown`() {
        val defaultLocale = Locale.getDefault()
        val modifiedContext = LocaleHelper.setLocale(context, "French")
        assertEquals(defaultLocale, Locale.getDefault())
        assertEquals(defaultLocale, modifiedContext.resources.configuration.locales.get(0))
    }
}
