package com.android.learnconnect

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.learnconnect.data.locale.LocaleHelper
import com.android.learnconnect.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var navState: Bundle? = null // NavController durumu için değişken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySavedTheme()

        // NavHostFragment ve NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // NavController durumunu geri yükleme
        if (savedInstanceState != null) {
            navState = savedInstanceState.getBundle("nav_state")
            if (navState != null) {
                navController.restoreState(navState)
            }
        }

        // Kullanıcı giriş durumu kontrolü
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            if (savedInstanceState == null && navState == null) {
                // Uygulama ilk kez başlatılıyorsa ve kullanıcı giriş yapmışsa
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, false)
                    .build()
                navController.navigate(R.id.exploreFragment, null, navOptions)
            }
            // Aksi halde, navigasyon durumu zaten restore edildiği için ekstra bir işlem yapmaya gerek yok
        } else {
            if (savedInstanceState == null && navState == null) {
                // Kullanıcı giriş yapmamışsa ve uygulama ilk kez başlatılıyorsa
                navController.navigate(R.id.welcomeFragment)
            }
        }

        // BottomNavigation görünürlüğü ayarları
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.exploreFragment, R.id.myCourseFragment, R.id.profileFragment -> {
                    binding.navView.visibility = View.VISIBLE
                }
                else -> {
                    binding.navView.visibility = View.GONE
                }
            }
        }

        // BottomNavigation ile NavController bağlantısı
        binding.navView.setupWithNavController(navController)
    }

    private fun applySavedTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedTheme = sharedPreferences.getInt("selected_theme", 2) // Varsayılan olarak System Default

        val mode = when (selectedTheme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO // Light
            1 -> AppCompatDelegate.MODE_NIGHT_YES // Dark
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // System Default
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // NavController durumunu kaydetme
        navState = navController.saveState()
        outState.putBundle("nav_state", navState)
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val language = sharedPreferences.getString("selected_language", "English") ?: "English"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, language))
    }
}
