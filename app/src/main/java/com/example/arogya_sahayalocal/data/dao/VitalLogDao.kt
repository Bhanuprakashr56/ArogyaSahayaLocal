package com.example.arogya_sahayalocal.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arogya_sahayalocal.data.entity.VitalLogEntity

@Dao
interface VitalLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vital: VitalLogEntity)

    @Query("SELECT * FROM vital_logs ORDER BY timestamp DESC LIMIT 7")
    fun getLast7Days(): LiveData<List<VitalLogEntity>>

    @Query("SELECT * FROM vital_logs ORDER BY timestamp DESC LIMIT 7")
    suspend fun getLast7DaysSync(): List<VitalLogEntity>
}
