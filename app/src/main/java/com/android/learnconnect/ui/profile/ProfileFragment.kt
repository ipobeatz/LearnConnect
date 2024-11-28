package com.android.learnconnect.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.android.learnconnect.R
import com.android.learnconnect.data.locale.LocaleHelper
import com.android.learnconnect.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor() : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSettingsList()
        setupRecyclerView()
        setupUserProfile()
        setupLogoutButton()
    }

    private fun setupSettingsList() {
        settingsList = listOf(
            getString(R.string.language_preferences),
            getString(R.string.theme_settings),
            getString(R.string.downloaded_videos),
            getString(R.string.about_learnconnect)
        )
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ProfileAdapter(settingsList) { item ->
            handleItemClick(item)
        }
    }

    private fun setupUserProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val displayName = user.displayName ?: "Ad Soyad Bulunamadı"
            binding.accountName.text = displayName

            val email = user.email ?: "Mail Bulunamadı"
            binding.mailName.text = email

            val initials = getInitialsFromName(displayName)
            binding.profileInitials.text = initials
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            viewModel.performSignOut()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.exploreFragment, true).build()
            findNavController().navigate(
                R.id.action_profileFragment_to_welcomeFragment,
                null,
                navOptions
            )
        }
    }

    private fun handleItemClick(item: String) {
        when (item) {
            getString(R.string.language_preferences) -> showLanguageDialog()
            getString(R.string.theme_settings) -> showThemeDialog()
            getString(R.string.downloaded_videos) -> {
                findNavController().navigate(R.id.action_profileFragment_to_downloadedVideosFragment)
            }

            getString(R.string.about_learnconnect) -> showAboutLearnConnectDialog()
        }
    }

    private fun showAboutLearnConnectDialog() {
        AlertDialog.Builder(requireContext()).setTitle(getString(R.string.about_learnconnect))
            .setMessage(getString(R.string.learnconnect_description))
            .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }.show()
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        val currentTheme = getSharedPreferences().getInt("selected_theme", 2)

        AlertDialog.Builder(requireContext()).setTitle("Choose Theme")
            .setSingleChoiceItems(themes, currentTheme) { dialog, which ->
                applyThemeWithoutRestart(which)
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.show()
    }

    private fun applyThemeWithoutRestart(selectedTheme: Int) {
        val mode = when (selectedTheme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
        getSharedPreferences().edit().putInt("selected_theme", selectedTheme).apply()
        requireActivity().recreate()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Türkçe", "English")
        val currentLanguage = getSharedPreferences().getString("selected_language", "English")

        AlertDialog.Builder(requireContext()).setTitle("Dil Seçimi")
            .setSingleChoiceItems(languages, languages.indexOf(currentLanguage)) { dialog, which ->
                applyLanguage(languages[which])
                dialog.dismiss()
            }.setNegativeButton("İptal") { dialog, _ -> dialog.dismiss() }.show()
    }

    private fun applyLanguage(language: String) {
        LocaleHelper.setLocale(requireContext(), language)
        getSharedPreferences().edit().putString("selected_language", language).apply()
        requireActivity().recreate()
    }

    private fun getInitialsFromName(name: String): String {
        val words = name.split(" ")
        return if (words.size >= 2) {
            "${words[0].firstOrNull()?.uppercase() ?: ""}${
                words[1].firstOrNull()?.uppercase() ?: ""
            }"
        } else {
            name.firstOrNull()?.uppercase()?.toString() ?: ""
        }
    }

    private fun getSharedPreferences() =
        PreferenceManager.getDefaultSharedPreferences(requireContext())

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
