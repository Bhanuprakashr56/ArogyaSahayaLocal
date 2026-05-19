package com.example.arogya_sahayalocal.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.arogya_sahayalocal.databinding.ActivityAddMedicineBinding
import com.example.arogya_sahayalocal.ui.viewmodel.MedicineViewModel

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicineBinding
    private lateinit var viewModel: MedicineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Medicine"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Fix: AndroidViewModel requires Application — use the correct factory
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MedicineViewModel::class.java]

        binding.btnSaveMedicine.setOnClickListener {
            val name   = binding.etMedicineName.text.toString().trim()
            val dosage = binding.etDosage.text.toString().trim()
            val timing = when (binding.rgTiming.checkedRadioButtonId) {
                binding.rbMorning.id   -> "Morning"
                binding.rbAfternoon.id -> "Afternoon"
                binding.rbNight.id     -> "Night"
                else                   -> ""
            }

            if (name.isEmpty()) {
                binding.etMedicineName.error = "Medicine name required"
                return@setOnClickListener
            }
            if (dosage.isEmpty()) {
                binding.etDosage.error = "Dosage required"
                return@setOnClickListener
            }
            if (timing.isEmpty()) {
                Toast.makeText(this, "Please select a timing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addMedicine(name, dosage, timing)
            Toast.makeText(this, "✓ $name reminder set for $timing", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
