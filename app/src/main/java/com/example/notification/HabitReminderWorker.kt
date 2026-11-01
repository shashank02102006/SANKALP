package com.example.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.local.AppDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class HabitReminderWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val db = AppDatabase.getInstance(appContext)
            val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

            val activeHabits = db.habitDao().getActiveHabitsCount()
            if (activeHabits > 0) {
                val helper = NotificationHelper(appContext)
                helper.showHabitReminder(
                    habitTitle = "Daily Mission Readiness",
                    habitDescription = "Keep your momentum unbroken. Review and complete today's discipline objectives."
                )
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "sankalp_daily_discipline_reminder"

        fun scheduleDailySync(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<HabitReminderWorker>(
                12, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        fun cancelDailySync(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
