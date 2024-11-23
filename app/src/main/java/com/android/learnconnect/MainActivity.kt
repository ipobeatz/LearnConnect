package com.android.learnconnect

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.learnconnect.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FragmentContainerView yüklendikten sonra NavController'a erişim
        binding.root.post {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            binding.navView.setupWithNavController(navController)

            // Kullanıcının giriş durumu
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                navController.navigate(R.id.homeFragment) // Sadece oturum açıkken yönlendirme yap
            }

            // Navigation Listener
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment, R.id.myCourseFragment, R.id.profileFragment -> {
                        binding.navView.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.navView.visibility = View.GONE
                    }
                }
            }
        }
    }
}
