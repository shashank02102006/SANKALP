package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultData
import com.example.data.model.HabitCompletionEntity
import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity
import com.example.data.repository.DayCompletionSummary
import com.example.data.repository.HabitRepository
import com.example.data.repository.HabitWithStatus
import com.example.data.repository.UserPreferencesRepository
import com.example.data.repository.WeeklyAnalytics
import com.example.notification.HabitReminderScheduler
import com.example.notification.HabitReminderWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val habitRepository = HabitRepository(db)
    val preferencesRepository = UserPreferencesRepository(application)
    private val reminderScheduler = HabitReminderScheduler(application)

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val userName: StateFlow<String> = preferencesRepository.userName
    val onboardingCompleted: StateFlow<Boolean> = preferencesRepository.onboardingCompleted
    val notificationsEnabled: StateFlow<Boolean> = preferencesRepository.notificationsEnabled

    val activeHabits: StateFlow<List<HabitEntity>> = habitRepository.activeHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCompletions: StateFlow<List<HabitCompletionEntity>> = habitRepository.allCompletions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuotes: StateFlow<List<QuoteEntity>> = habitRepository.allQuotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultData.motivationalQuotes)

    val favoriteQuotes: StateFlow<List<QuoteEntity>> = habitRepository.favoriteQuotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Habits with completion status for selected date
    @OptIn(ExperimentalCoroutinesApi::class)
    val habitsForSelectedDate: StateFlow<List<HabitWithStatus>> = _selectedDate
        .flatMapLatest { date ->
            habitRepository.getHabitsWithStatusForDate(date)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Today's habits (specifically for today's dashboard)
    val todayHabits: StateFlow<List<HabitWithStatus>> = habitRepository
        .getHabitsWithStatusForDate(LocalDate.now())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Overall global streak calculation (days with >=1 completed habit)
    val globalStreaks: StateFlow<Pair<Int, Int>> = allCompletions.combine(_selectedDate) { completions, _ ->
        val uniqueDates = completions.map { it.date }.toSet()
        HabitRepository.calculateOverallStreak(uniqueDates, LocalDate.now())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0 to 0)

    // Weekly Analytics (last 7 days)
    val weeklyAnalytics: StateFlow<WeeklyAnalytics> = combine(
        activeHabits,
        allCompletions
    ) { habits, completions ->
        val today = LocalDate.now()
        val totalActive = habits.size.coerceAtLeast(1)
        val completionsByDate = completions.groupBy { it.date }

        val daySummaries = (6 downTo 0).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val dayLabel = date.dayOfWeek.name.take(3)
            val completedCount = completionsByDate[dateStr]?.map { it.habitId }?.distinct()?.size ?: 0
            val percentage = ((completedCount.toFloat() / totalActive) * 100).toInt().coerceIn(0, 100)

            DayCompletionSummary(
                date = date,
                dateString = dateStr,
                dayLabel = dayLabel,
                completedCount = completedCount,
                totalHabits = totalActive,
                percentage = percentage
            )
        }

        val totalCompletionsWeek = daySummaries.sumOf { it.completedCount }
        val avgPercentage = if (daySummaries.isNotEmpty()) {
            daySummaries.map { it.percentage }.average().toInt()
        } else 0
        val bestDay = daySummaries.maxByOrNull { it.percentage }

        WeeklyAnalytics(
            days = daySummaries,
            averagePercentage = avgPercentage,
            totalCompletionsThisWeek = totalCompletionsWeek,
            bestDay = bestDay
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WeeklyAnalytics(emptyList(), 0, 0, null)
    )

    // Daily quote rotation
    val dailyQuote: StateFlow<QuoteEntity> = allQuotes.combine(_selectedDate) { quotes, _ ->
        if (quotes.isEmpty()) {
            DefaultData.motivationalQuotes.first()
        } else {
            val dayOfYear = LocalDate.now().dayOfYear
            quotes[dayOfYear % quotes.size]
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DefaultData.motivationalQuotes.first()
    )

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun toggleHabitCompletion(habitId: Long, date: LocalDate = _selectedDate.value) {
        viewModelScope.launch {
            habitRepository.toggleHabitCompletion(habitId, date)
        }
    }

    fun createHabit(
        title: String,
        description: String,
        category: String,
        frequency: String,
        customDays: String = "MON,TUE,WED,THU,FRI,SAT,SUN",
        reminderTime: String? = null,
        isReminderEnabled: Boolean = false
    ) {
        viewModelScope.launch {
            val newHabit = HabitEntity(
                title = title.trim(),
                description = description.trim(),
                category = category,
                frequency = frequency,
                customDays = customDays,
                reminderTime = reminderTime,
                isReminderEnabled = isReminderEnabled
            )
            val id = habitRepository.insertHabit(newHabit)
            if (isReminderEnabled && reminderTime != null) {
                reminderScheduler.scheduleHabitReminder(id, title, description, reminderTime)
            }
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitRepository.updateHabit(habit)
            if (habit.isReminderEnabled && habit.reminderTime != null) {
                reminderScheduler.scheduleHabitReminder(
                    habit.id,
                    habit.title,
                    habit.description,
                    habit.reminderTime
                )
            } else {
                reminderScheduler.cancelHabitReminder(habit.id)
            }
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            reminderScheduler.cancelHabitReminder(habit.id)
            habitRepository.deleteHabit(habit)
        }
    }

    fun toggleFavoriteQuote(quoteId: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            habitRepository.toggleFavoriteQuote(quoteId, !currentIsFavorite)
        }
    }

    fun completeOnboarding(name: String) {
        preferencesRepository.setUserName(name)
        preferencesRepository.setOnboardingCompleted(true)
        HabitReminderWorker.scheduleDailySync(getApplication())
    }

    fun updateUserName(name: String) {
        preferencesRepository.setUserName(name)
    }

    fun toggleNotifications(enabled: Boolean) {
        preferencesRepository.setNotificationsEnabled(enabled)
        if (enabled) {
            HabitReminderWorker.scheduleDailySync(getApplication())
        } else {
            HabitReminderWorker.cancelDailySync(getApplication())
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            habitRepository.resetAllData()
            preferencesRepository.resetPreferences()
        }
    }
}
