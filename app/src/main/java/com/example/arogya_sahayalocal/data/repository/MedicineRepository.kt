package com.example.arogya_sahayalocal.data.repository

import androidx.lifecycle.LiveData
import com.example.arogya_sahayalocal.data.dao.MedicineDao
import com.example.arogya_sahayalocal.data.entity.MedicineEntity

class MedicineRepository(private val dao: MedicineDao) {
    val activeMedicines: LiveData<List<MedicineEntity>> = dao.getActiveMedicines()

    suspend fun insert(medicine: MedicineEntity): Long = dao.insert(medicine)
    suspend fun delete(medicine: MedicineEntity) = dao.delete(medicine)
}
