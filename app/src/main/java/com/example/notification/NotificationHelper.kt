package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class NotificationHelper(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_HABITS = "sankalp_habits_channel"
        const val CHANNEL_FOCUS = "sankalp_focus_channel"
        const val NOTIFICATION_ID_FOCUS = 1001
        const val NOTIFICATION_ID_DAILY = 1002
    }

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val habitChannel = NotificationChannel(
                CHANNEL_HABITS,
                "Habit & Discipline Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily discipline mission briefings and habit check-in alerts."
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
            }

            val focusChannel = NotificationChannel(
                CHANNEL_FOCUS,
                "Focus Session Completed",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when your deep focus drill concludes."
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            notificationManager.createNotificationChannel(habitChannel)
            notificationManager.createNotificationChannel(focusChannel)
        }
    }

    fun showHabitReminder(habitTitle: String, habitDescription: String = "") {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            habitTitle.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_HABITS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("MISSION ALERT: $habitTitle")
            .setContentText(
                if (habitDescription.isNotBlank()) habitDescription
                else "Discipline builds destiny. Execute your objective today."
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        if (habitDescription.isNotBlank()) "$habitDescription\n\nDiscipline builds destiny. What you conquer in private defines who you become in public."
                        else "Discipline builds destiny. Execute your daily objective now and maintain your unbroken streak."
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), notification)
    }

    fun showFocusCompletedNotification(durationMinutes: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            9999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_FOCUS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("MISSION ACCOMPLISHED")
            .setContentText("Completed $durationMinutes min of unbroken tactical focus.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Great work, Commander! You have forged $durationMinutes minutes of unbroken deep focus. Consistency creates unstoppable momentum.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID_FOCUS, notification)
    }
}
