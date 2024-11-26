package com.android.learnconnect

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.learnconnect.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavHostFragment ve NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Kullanıcı giriş durumu kontrolü
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Kullanıcı giriş yaptıysa ExploreFragment'ten başla
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.welcomeFragment, true) // loginFragment'a kadar olan tüm fragmentları dahil ederek stack'ten çıkar
                .build()
            navController.setGraph(R.navigation.mobile_navigation, null) // Varsayılan grafiği yeniden başlat
            navController.navigate(R.id.exploreFragment, null, navOptions)
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

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

}
