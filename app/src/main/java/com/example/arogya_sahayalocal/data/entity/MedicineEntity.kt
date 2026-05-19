package com.example.arogya_sahayalocal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val timing: String,       // "Morning" | "Afternoon" | "Night"
    val isActive: Boolean = true,
    val workerId: String = ""
)
