package com.example.arogya_sahayalocal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital_logs")
data class VitalLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val systolic: Int,
    val diastolic: Int,
    val heartRate: Int,
    val date: String,           // "yyyy-MM-dd"
    val timestamp: Long = System.currentTimeMillis()
)
