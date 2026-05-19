package com.example.arogya_sahayalocal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arogya_sahayalocal.databinding.ActivityEmergencyBinding

class EmergencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding
    private val PREFS = "medical_profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Emergency SOS"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs          = getSharedPreferences(PREFS, MODE_PRIVATE)
        val patientName    = prefs.getString("name", "Patient") ?: "Patient"
        val conditions     = prefs.getString("conditions", "—") ?: "—"
        val emergencyPhone = prefs.getString("emergency", "") ?: ""

        binding.tvPatientSummary.text = "Patient: $patientName\nConditions: $conditions"

        binding.btnSos.setOnClickListener {
            if (emergencyPhone.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("🚨 Call Emergency Contact?")
                    .setMessage("Calling $emergencyPhone\n\n$patientName needs help!")
                    .setPositiveButton("CALL NOW") { _, _ ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$emergencyPhone"))
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("🚨 Emergency Services")
                    .setMessage("No emergency contact saved.\nCall 108 (Ambulance)?")
                    .setPositiveButton("CALL 108") { _, _ ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        binding.btnCall108.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
            startActivity(intent)
        }

        binding.btnCallContact.setOnClickListener {
            if (emergencyPhone.isEmpty()) {
                Toast.makeText(this, "Save emergency contact in Medical Profile first", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$emergencyPhone"))
                startActivity(intent)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
