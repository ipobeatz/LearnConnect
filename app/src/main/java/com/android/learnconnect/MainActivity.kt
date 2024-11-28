package com.android.learnconnect

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.learnconnect.data.locale.LocaleHelper
import com.android.learnconnect.databinding.ActivityMainBinding
import com.android.learnconnect.util.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var navState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.applySavedTheme(this)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        if (savedInstanceState != null) {
            navState = savedInstanceState.getBundle("nav_state")
            if (navState != null) {
                navController.restoreState(navState)
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            if (savedInstanceState == null && navState == null) {
                val navOptions = NavOptions.Builder().setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, false).build()
                navController.navigate(R.id.exploreFragment, null, navOptions)
            }
        } else {
            if (savedInstanceState == null && navState == null) {

                navController.navigate(R.id.welcomeFragment)
            }
        }


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

        binding.navView.setupWithNavController(navController)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

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
