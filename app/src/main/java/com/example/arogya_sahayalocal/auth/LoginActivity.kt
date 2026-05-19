package com.example.arogya_sahayalocal.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arogya_sahayalocal.databinding.ActivityLoginBinding
import com.example.arogya_sahayalocal.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (!validateInputs(email, password)) return@setOnClickListener
            showLoading(true)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    showLoading(false)
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_LONG).show()
                            user?.sendEmailVerification()
                            auth.signOut()
                        }
                    } else {
                        val errorMsg = when {
                            task.exception?.message?.contains("no user record") == true ||
                                    task.exception?.message?.contains("user-not-found") == true ||
                                    task.exception?.message?.contains("EMAIL_NOT_FOUND") == true ||
                                    task.exception?.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                                "This email is not registered. Please sign up first."

                            task.exception?.message?.contains("password is invalid") == true ||
                                    task.exception?.message?.contains("wrong-password") == true ||
                                    task.exception?.message?.contains("INVALID_PASSWORD") == true ->
                                "Incorrect password. Please try again."

                            task.exception is FirebaseAuthInvalidUserException ->
                                "This email is not registered. Please sign up first."

                            task.exception is FirebaseAuthInvalidCredentialsException ->
                                "Incorrect password. Please try again."

                            else -> "Login failed: ${task.exception?.message}"
                        }
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Min 6 characters"
            return false
        }
        return true
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
    }
}