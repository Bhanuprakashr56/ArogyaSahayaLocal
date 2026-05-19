package com.example.arogya_sahayalocal.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arogya_sahayalocal.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val name     = binding.etFullName.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val phone    = binding.etPhone.text.toString().trim()
            val age      = binding.etAge.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirm  = binding.etConfirmPassword.text.toString().trim()

            if (!validateInputs(name, email, phone, age, password, confirm)) return@setOnClickListener
            showLoading(true)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser!!
                        user.sendEmailVerification()
                            .addOnCompleteListener { verifyTask ->
                                if (verifyTask.isSuccessful) {
                                    saveUserToFirestore(user.uid, name, email, phone, age)
                                } else {
                                    showLoading(false)
                                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        showLoading(false)
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String, phone: String, age: String) {
        val userMap = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "age" to age,
            "createdAt" to com.google.firebase.Timestamp.now()
        )
        firestore.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                showLoading(false)
                Toast.makeText(this, "Account created! Please verify your email.", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                showLoading(false)
                Toast.makeText(this, "Profile save failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateInputs(
        name: String, email: String, phone: String,
        age: String, password: String, confirm: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.etFullName.error = "Name is required"
            return false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            return false
        }
        if (phone.isEmpty() || phone.length < 10) {
            binding.etPhone.error = "Enter valid 10-digit phone"
            return false
        }
        if (age.isEmpty()) {
            binding.etAge.error = "Age is required"
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            binding.etPassword.error = "Min 6 characters"
            return false
        }
        if (password != confirm) {
            binding.etConfirmPassword.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }
}
