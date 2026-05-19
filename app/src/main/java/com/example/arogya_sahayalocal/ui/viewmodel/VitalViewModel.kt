package com.example.arogya_sahayalocal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.arogya_sahayalocal.data.AppDatabase
import com.example.arogya_sahayalocal.data.entity.VitalLogEntity
import com.example.arogya_sahayalocal.data.repository.VitalRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VitalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VitalRepository(
        AppDatabase.getInstance(application).vitalLogDao()
    )
    val last7Days = repository.last7Days

    fun logVitals(systolic: Int, diastolic: Int, heartRate: Int) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            repository.insert(
                VitalLogEntity(
                    systolic  = systolic,
                    diastolic = diastolic,
                    heartRate = heartRate,
                    date      = date
                )
            )
        }
    }
}
