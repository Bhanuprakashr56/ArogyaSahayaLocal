package com.example.arogya_sahayalocal.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.arogya_sahayalocal.data.entity.MedicineEntity

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity): Long

    @Delete
    suspend fun delete(medicine: MedicineEntity)

    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY timing ASC")
    fun getActiveMedicines(): LiveData<List<MedicineEntity>>
}
