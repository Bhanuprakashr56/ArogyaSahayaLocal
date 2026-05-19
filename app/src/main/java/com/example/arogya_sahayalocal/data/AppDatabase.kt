package com.example.arogya_sahayalocal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.arogya_sahayalocal.data.dao.MedicineDao
import com.example.arogya_sahayalocal.data.dao.VitalLogDao
import com.example.arogya_sahayalocal.data.entity.MedicineEntity
import com.example.arogya_sahayalocal.data.entity.VitalLogEntity

@Database(
    entities = [MedicineEntity::class, VitalLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun vitalLogDao(): VitalLogDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "arogya_sahaya_db"
                ).build().also { INSTANCE = it }
            }
    }
}
