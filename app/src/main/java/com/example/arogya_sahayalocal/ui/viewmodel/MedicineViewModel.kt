package com.example.arogya_sahayalocal.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.arogya_sahayalocal.data.AppDatabase
import com.example.arogya_sahayalocal.data.entity.MedicineEntity
import com.example.arogya_sahayalocal.data.repository.MedicineRepository
import com.example.arogya_sahayalocal.worker.MedicineReminderWorker
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MedicineRepository(
        AppDatabase.getInstance(application).medicineDao()
    )
    val medicines = repository.activeMedicines

    fun addMedicine(name: String, dosage: String, timing: String) {
        viewModelScope.launch {
            val workerId = scheduleReminder(name, dosage, timing)
            repository.insert(
                MedicineEntity(name = name, dosage = dosage, timing = timing, workerId = workerId)
            )
        }
    }

    fun deleteMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            if (medicine.workerId.isNotEmpty()) {
                WorkManager.getInstance(getApplication()).cancelUniqueWork(medicine.workerId)
            }
            repository.delete(medicine)
        }
    }

    private fun scheduleReminder(name: String, dosage: String, timing: String): String {
        val workerId = "med_${name}_${timing}_${System.currentTimeMillis()}"

        val targetHour = when (timing) {
            "Morning"   -> 8
            "Afternoon" -> 13
            "Night"     -> 20
            else        -> 8
        }

        val data = workDataOf(
            MedicineReminderWorker.KEY_MEDICINE_NAME to name,
            MedicineReminderWorker.KEY_DOSAGE        to dosage,
            MedicineReminderWorker.KEY_TIMING        to timing
        )

        val request = PeriodicWorkRequestBuilder<MedicineReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calculateDelayMillis(targetHour), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            workerId,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        return workerId
    }

    /** Returns milliseconds from now until the next occurrence of targetHour:00 */
    private fun calculateDelayMillis(targetHour: Int): Long {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (cal.timeInMillis <= now) cal.add(Calendar.DAY_OF_YEAR, 1)
        return cal.timeInMillis - now
    }
}
