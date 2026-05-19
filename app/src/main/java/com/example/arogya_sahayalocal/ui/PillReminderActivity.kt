package com.example.arogya_sahayalocal.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arogya_sahayalocal.databinding.ActivityPillReminderBinding
import com.example.arogya_sahayalocal.ui.adapter.MedicineAdapter
import com.example.arogya_sahayalocal.ui.viewmodel.MedicineViewModel

class PillReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPillReminderBinding
    private lateinit var viewModel: MedicineViewModel
    private lateinit var adapter: MedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPillReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Pill Reminders"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Fix: AndroidViewModel requires Application — use the correct factory
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MedicineViewModel::class.java]

        adapter = MedicineAdapter { medicine ->
            viewModel.deleteMedicine(medicine)
            Toast.makeText(this, "${medicine.name} removed", Toast.LENGTH_SHORT).show()
        }
        binding.rvMedicines.layoutManager = LinearLayoutManager(this)
        binding.rvMedicines.adapter = adapter

        viewModel.medicines.observe(this) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility =
                if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        binding.fabAddMedicine.setOnClickListener {
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
