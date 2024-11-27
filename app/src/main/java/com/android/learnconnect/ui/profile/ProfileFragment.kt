package com.android.learnconnect.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor() : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val settingsList = listOf(
        "Dil Tercihleri",
        "Tema Ayarları",
        "LearnConnect Hakkında"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProfileAdapter(settingsList) { item ->
            handleItemClick(item)
        }


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val displayName = user.displayName ?: "Ad Soyad Bulunamadı"
            binding.accountName.text = displayName
            Log.d("FirebaseUser", "Giriş Yapan Kullanıcı: $displayName")

            val email = user.email ?: "Mail Bulunamadı"
            binding.mailName.text = email
            Log.d("FirebaseUser", "Kullanıcı Mail: $email")

            val initials = getInitialsFromName(displayName)
            binding.profileInitials.text = initials
            Log.d("FirebaseUser", "Kullanıcı Harfler: $initials")
        }




        binding.btnLogout.setOnClickListener {
            viewModel.performSignOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.exploreFragment, true) // nav_graph_id, Navigation Graph'ınızın kendisinin ID'si olmalıdır
                .build()

            findNavController().navigate(R.id.action_profileFragment_to_welcomeFragment, null, navOptions)
        }
    }
    private fun handleItemClick(item: String) {
        when (item) {
            "Dil Tercihleri" -> {
                // Dil tercihleri ekranına yönlendirme yap
            }
            "Tema Ayarları" -> {
                // Tema ayarlarını aç
                showThemeDialog()
            }
            "LearnConnect Hakkında" -> {
                // LearnConnect hakkında bilgiyi göster
            }
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val currentTheme = sharedPreferences.getInt("selected_theme", 2) // Varsayılan olarak System Default

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Theme")
        builder.setSingleChoiceItems(themes, currentTheme) { dialog, which ->
            applyThemeWithoutRestart(which)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun getInitialsFromName(name: String): String {
        val words = name.split(" ") // İsmi boşluklardan ayır
        return if (words.size >= 2) {
            // İlk iki kelimenin baş harflerini birleştir
            "${words[0].firstOrNull()?.uppercase() ?: ""}${words[1].firstOrNull()?.uppercase() ?: ""}"
        } else {
            // Eğer tek bir kelime varsa sadece onun ilk harfini al
            name.firstOrNull()?.uppercase()?.toString() ?: ""
        }
    }



    private fun applyThemeWithoutRestart(selectedTheme: Int) {
        val mode = when (selectedTheme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO // Light
            1 -> AppCompatDelegate.MODE_NIGHT_YES // Dark
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // System Default
        }

        AppCompatDelegate.setDefaultNightMode(mode)

        // Tema tercihlerini kaydet
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPreferences.edit()) {
            putInt("selected_theme", selectedTheme)
            apply()
        }

        // Aktiviteyi yeniden oluştur
        requireActivity().recreate()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}