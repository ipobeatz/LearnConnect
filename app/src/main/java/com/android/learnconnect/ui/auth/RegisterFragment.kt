package com.android.learnconnect.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.learnconnect.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View Binding ile layout'u bağlama
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button işlemleri burada yapılır
        binding.btnRegister.setOnClickListener {
            val name = binding.etFirstName.text.toString().trim()
            val surname = binding.etLastName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fullName = "$name $surname" // Ad ve Soyadı birleştir

            // Firebase'e kayıt işlemi
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Kullanıcı başarıyla oluşturuldu, ad soyadı ekleniyor
                        val user = FirebaseAuth.getInstance().currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName) // Ad ve Soyadı burada ekliyoruz
                            .build()

                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(requireContext(), "Hesap başarıyla oluşturuldu", Toast.LENGTH_SHORT).show()
                                this.requireActivity().onBackPressed()
                            } else {
                                Toast.makeText(requireContext(), "Ad soyad eklenirken hata oluştu", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Hata: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp() // Bir önceki ekrana dön
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
