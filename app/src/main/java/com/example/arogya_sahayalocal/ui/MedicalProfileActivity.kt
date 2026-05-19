package com.example.arogya_sahayalocal.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arogya_sahayalocal.databinding.ActivityMedicalProfileBinding

class MedicalProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalProfileBinding
    private val PREFS = "medical_profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicalProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Medical Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadProfile()

        binding.btnSaveProfile.setOnClickListener {
            val name       = binding.etPatientName.text.toString().trim()
            val age        = binding.etAge.text.toString().trim()
            val conditions = binding.etConditions.text.toString().trim()
            val emergency  = binding.etEmergencyContact.text.toString().trim()

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please fill Name and Age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString("name", name)
                .putString("age", age)
                .putString("conditions", conditions)
                .putString("emergency", emergency)
                .apply()
            Toast.makeText(this, "Profile Saved ✓", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfile() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        binding.etPatientName.setText(prefs.getString("name", ""))
        binding.etAge.setText(prefs.getString("age", ""))
        binding.etConditions.setText(prefs.getString("conditions", ""))
        binding.etEmergencyContact.setText(prefs.getString("emergency", ""))
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
