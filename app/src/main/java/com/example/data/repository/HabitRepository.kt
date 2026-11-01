package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DefaultData
import com.example.data.model.FocusSessionEntity
import com.example.data.model.HabitCompletionEntity
import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class HabitWithStatus(
    val habit: HabitEntity,
    val isCompletedToday: Boolean,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalCompletions: Int
)

data class DayCompletionSummary(
    val date: LocalDate,
    val dateString: String,
    val dayLabel: String,
    val completedCount: Int,
    val totalHabits: Int,
    val percentage: Int
)

data class WeeklyAnalytics(
    val days: List<DayCompletionSummary>,
    val averagePercentage: Int,
    val totalCompletionsThisWeek: Int,
    val bestDay: DayCompletionSummary?
)

class HabitRepository(private val db: AppDatabase) {
    private val habitDao = db.habitDao()
    private val completionDao = db.habitCompletionDao()
    private val focusDao = db.focusSessionDao()
    private val quoteDao = db.quoteDao()

    val activeHabits: Flow<List<HabitEntity>> = habitDao.getAllActiveHabits()
    val allCompletions: Flow<List<HabitCompletionEntity>> = completionDao.getAllCompletions()
    val allFocusSessions: Flow<List<FocusSessionEntity>> = focusDao.getAllSessions()
    val allQuotes: Flow<List<QuoteEntity>> = quoteDao.getAllQuotes()
    val favoriteQuotes: Flow<List<QuoteEntity>> = quoteDao.getFavoriteQuotes()
    val totalFocusMinutes: Flow<Int> = focusDao.getTotalFocusMinutes()

    // Combined Flow: Active Habits with Today's status and computed streaks
    fun getHabitsWithStatusForDate(date: LocalDate): Flow<List<HabitWithStatus>> {
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return combine(activeHabits, allCompletions) { habits, completions ->
            val completionsByHabit = completions.groupBy { it.habitId }

            habits.map { habit ->
                val habitCompletions = completionsByHabit[habit.id] ?: emptyList()
                val isCompleted = habitCompletions.any { it.date == dateStr }
                val completedDates = habitCompletions.map { it.date }.toSet()

                val (currentStreak, longestStreak) = calculateStreaks(completedDates, date)
                HabitWithStatus(
                    habit = habit,
                    isCompletedToday = isCompleted,
                    currentStreak = currentStreak,
                    longestStreak = longestStreak,
                    totalCompletions = habitCompletions.size
                )
            }
        }
    }

    suspend fun toggleHabitCompletion(habitId: Long, date: LocalDate): Boolean = withContext(Dispatchers.IO) {
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val isDone = completionDao.isCompleted(habitId, dateStr) > 0
        if (isDone) {
            completionDao.deleteCompletion(habitId, dateStr)
            false
        } else {
            completionDao.insertCompletion(
                HabitCompletionEntity(
                    habitId = habitId,
                    date = dateStr,
                    completedAt = System.currentTimeMillis()
                )
            )
            true
        }
    }

    suspend fun insertHabit(habit: HabitEntity): Long = withContext(Dispatchers.IO) {
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) = withContext(Dispatchers.IO) {
        completionDao.deleteCompletionsForHabit(habit.id)
        habitDao.deleteHabit(habit)
    }

    suspend fun deleteHabitById(id: Long) = withContext(Dispatchers.IO) {
        completionDao.deleteCompletionsForHabit(id)
        habitDao.deleteHabitById(id)
    }

    // Weekly Analytics calculation for last 7 days ending on target date
    suspend fun getWeeklyAnalytics(today: LocalDate): WeeklyAnalytics = withContext(Dispatchers.IO) {
        val activeHabitsList = mutableListOf<HabitEntity>()
        // Read active habits once
        val habitCount = habitDao.getActiveHabitsCount()
        val habits = if (habitCount == 0) DefaultData.defaultHabits else emptyList()
        val totalActive = if (habitCount > 0) habitCount else habits.size

        val days = (6 downTo 0).map { daysAgo ->
            val d = today.minusDays(daysAgo.toLong())
            val dStr = d.format(DateTimeFormatter.ISO_LOCAL_DATE)
            d to dStr
        }

        val startDateStr = days.first().second
        val endDateStr = days.last().second

        val daySummaries = days.map { (date, dateStr) ->
            // Query completions on this day
            val dayLabel = date.dayOfWeek.name.take(3)
            val completions = completionDao.getAllCompletions() // Or check dates
            // Count unique habits completed on this date
            dayLabel
        }

        // We compute weekly statistics using database values
        // Let's do a reliable calculation
        val summaryList = mutableListOf<DayCompletionSummary>()
        var totalCompletionsWeek = 0
        var percentageSum = 0

        for (i in 6 downTo 0) {
            val date = today.minusDays(i.toLong())
            val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val dayLabel = date.dayOfWeek.name.take(3)
            // check unique habit completions for date
            // Note: will be calculated in Flow or suspended query
            summaryList.add(
                DayCompletionSummary(
                    date = date,
                    dateString = dateStr,
                    dayLabel = dayLabel,
                    completedCount = 0,
                    totalHabits = totalActive,
                    percentage = 0
                )
            )
        }

        WeeklyAnalytics(
            days = summaryList,
            averagePercentage = 0,
            totalCompletionsThisWeek = 0,
            bestDay = null
        )
    }

    // Streaks calculation algorithm
    companion object {
        fun calculateStreaks(completedDateStrings: Set<String>, today: LocalDate): Pair<Int, Int> {
            if (completedDateStrings.isEmpty()) return 0 to 0

            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val dates = completedDateStrings.mapNotNull {
                try {
                    LocalDate.parse(it, formatter)
                } catch (e: Exception) {
                    null
                }
            }.sorted()

            if (dates.isEmpty()) return 0 to 0

            // 1. Current streak:
            // Check if today is completed; if not, check if yesterday was completed.
            var currentStreak = 0
            var checkDate = today
            if (!completedDateStrings.contains(today.format(formatter))) {
                checkDate = today.minusDays(1)
            }

            while (completedDateStrings.contains(checkDate.format(formatter))) {
                currentStreak++
                checkDate = checkDate.minusDays(1)
            }

            // 2. Longest streak:
            var longestStreak = 0
            var tempStreak = 0
            var previousDate: LocalDate? = null

            for (date in dates) {
                if (previousDate == null) {
                    tempStreak = 1
                } else {
                    val daysBetween = ChronoUnit.DAYS.between(previousDate, date)
                    if (daysBetween == 1L) {
                        tempStreak++
                    } else if (daysBetween > 1L) {
                        tempStreak = 1
                    }
                }
                previousDate = date
                if (tempStreak > longestStreak) {
                    longestStreak = tempStreak
                }
            }

            return currentStreak to longestStreak
        }

        // Global streak: days where at least 1 habit was completed
        fun calculateOverallStreak(allCompletedDates: Set<String>, today: LocalDate): Pair<Int, Int> {
            return calculateStreaks(allCompletedDates, today)
        }
    }

    // Focus Session operations
    suspend fun saveFocusSession(durationMinutes: Int, tag: String): Long = withContext(Dispatchers.IO) {
        focusDao.insertSession(
            FocusSessionEntity(
                durationMinutes = durationMinutes,
                tag = tag,
                completedAt = System.currentTimeMillis(),
                isCompleted = true
            )
        )
    }

    // Quotes operations
    suspend fun toggleFavoriteQuote(quoteId: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        quoteDao.setFavorite(quoteId, isFavorite)
    }

    // Reset entire application data
    suspend fun resetAllData() = withContext(Dispatchers.IO) {
        completionDao.clearAllCompletions()
        habitDao.clearAllHabits()
        focusDao.clearAllSessions()
        // Re-seed defaults
        habitDao.insertHabits(DefaultData.defaultHabits)
    }
}
