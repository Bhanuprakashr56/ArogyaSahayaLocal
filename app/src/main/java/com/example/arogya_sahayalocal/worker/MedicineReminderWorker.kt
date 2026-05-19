package com.example.arogya_sahayalocal.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.arogya_sahayalocal.R

class MedicineReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val KEY_MEDICINE_NAME = "medicine_name"
        const val KEY_DOSAGE = "dosage"
        const val KEY_TIMING = "timing"
        const val CHANNEL_ID = "medicine_reminder_channel"
    }

    override fun doWork(): Result {
        val name    = inputData.getString(KEY_MEDICINE_NAME) ?: "Medicine"
        val dosage  = inputData.getString(KEY_DOSAGE) ?: ""
        val timing  = inputData.getString(KEY_TIMING) ?: ""
        showNotification(name, dosage, timing)
        return Result.success()
    }

    private fun showNotification(name: String, dosage: String, timing: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Medicine Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Daily medication reminders" }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("💊 Time for your $timing medicine")
            .setContentText("$name  —  $dosage")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Please take your $timing dose:\n$name  ($dosage)"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
