package com.android.learnconnect

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
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
            navController.setGraph(R.navigation.mobile_navigation, null) // Varsayılan grafiği yeniden başlat
            navController.navigate(R.id.exploreFragment)
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

}
