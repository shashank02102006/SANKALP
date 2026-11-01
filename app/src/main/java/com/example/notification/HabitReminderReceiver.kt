package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val helper = NotificationHelper(context)
        val habitTitle = intent.getStringExtra(EXTRA_HABIT_TITLE) ?: "Daily Discipline Check"
        val habitDesc = intent.getStringExtra(EXTRA_HABIT_DESC) ?: ""

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule all active reminders after device reboot
            val scheduler = HabitReminderScheduler(context)
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getInstance(context)
                db.habitDao().getAllActiveHabits().collect { habits ->
                    habits.filter { it.isReminderEnabled && it.reminderTime != null }.forEach {
                        scheduler.scheduleHabitReminder(it.id, it.title, it.description, it.reminderTime!!)
                    }
                }
            }
        } else {
            helper.showHabitReminder(habitTitle, habitDesc)
        }
    }

    companion object {
        const val ACTION_REMINDER = "com.example.sankalp.ACTION_REMINDER"
        const val EXTRA_HABIT_ID = "extra_habit_id"
        const val EXTRA_HABIT_TITLE = "extra_habit_title"
        const val EXTRA_HABIT_DESC = "extra_habit_desc"
    }
}

class HabitReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleHabitReminder(habitId: Long, title: String, description: String, time24h: String) {
        try {
            val parts = time24h.split(":")
            if (parts.size != 2) return
            val hour = parts[0].toIntOrNull() ?: return
            val minute = parts[1].toIntOrNull() ?: return

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                // If time already passed today, set for tomorrow
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val intent = Intent(context, HabitReminderReceiver::class.java).apply {
                action = HabitReminderReceiver.ACTION_REMINDER
                putExtra(HabitReminderReceiver.EXTRA_HABIT_ID, habitId)
                putExtra(HabitReminderReceiver.EXTRA_HABIT_TITLE, title)
                putExtra(HabitReminderReceiver.EXTRA_HABIT_DESC, description)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                habitId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelHabitReminder(habitId: Long) {
        try {
            val intent = Intent(context, HabitReminderReceiver::class.java).apply {
                action = HabitReminderReceiver.ACTION_REMINDER
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                habitId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
