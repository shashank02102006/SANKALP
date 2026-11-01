package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String = "DISCIPLINE", // PHYSICAL, MIND, DISCIPLINE, NUTRITION, STUDY, ROUTINE
    val frequency: String = "DAILY",     // DAILY, WEEKDAYS, WEEKENDS, CUSTOM
    val customDays: String = "MON,TUE,WED,THU,FRI,SAT,SUN",
    val reminderTime: String? = null,    // "HH:mm" (24h)
    val isReminderEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

@Entity(
    tableName = "habit_completions",
    indices = [
        Index(value = ["habitId", "date"], unique = true),
        Index(value = ["date"])
    ]
)
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String, // "yyyy-MM-dd"
    val completedAt: Long = System.currentTimeMillis(),
    val note: String? = null
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val durationMinutes: Int,
    val tag: String = "Deep Focus",
    val completedAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = true
)

@Entity(tableName = "motivational_quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quote: String,
    val author: String,
    val isFavorite: Boolean = false
)
