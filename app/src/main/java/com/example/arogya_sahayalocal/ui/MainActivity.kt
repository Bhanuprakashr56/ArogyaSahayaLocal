package com.example.arogya_sahayalocal.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arogya_sahayalocal.auth.SplashActivity
import com.example.arogya_sahayalocal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Feature card navigation
        binding.cardPillReminder.setOnClickListener {
            startActivity(Intent(this, PillReminderActivity::class.java))
        }

        binding.cardVitalLog.setOnClickListener {
            startActivity(Intent(this, VitalLogActivity::class.java))
        }

        binding.cardMedicalProfile.setOnClickListener {
            startActivity(Intent(this, MedicalProfileActivity::class.java))
        }

        binding.cardAshaConnect.setOnClickListener {
            startActivity(Intent(this, AshaConnectActivity::class.java))
        }

        binding.cardEmergency.setOnClickListener {
            startActivity(Intent(this, EmergencyActivity::class.java))
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SplashActivity::class.java))
            finishAffinity()
        }
    }
}
