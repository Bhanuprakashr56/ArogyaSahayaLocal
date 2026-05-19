package com.example.arogya_sahayalocal.data.repository

import androidx.lifecycle.LiveData
import com.example.arogya_sahayalocal.data.dao.VitalLogDao
import com.example.arogya_sahayalocal.data.entity.VitalLogEntity

class VitalRepository(private val dao: VitalLogDao) {
    val last7Days: LiveData<List<VitalLogEntity>> = dao.getLast7Days()

    suspend fun insert(vital: VitalLogEntity) = dao.insert(vital)
    suspend fun getLast7DaysSync(): List<VitalLogEntity> = dao.getLast7DaysSync()
}
