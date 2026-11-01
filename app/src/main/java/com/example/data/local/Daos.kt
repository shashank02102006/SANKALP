package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FocusSessionEntity
import com.example.data.model.HabitCompletionEntity
import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY id ASC")
    fun getAllActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>): List<Long>

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)

    @Query("SELECT COUNT(*) FROM habits WHERE isArchived = 0")
    suspend fun getActiveHabitsCount(): Int

    @Query("DELETE FROM habits")
    suspend fun clearAllHabits()
}

@Dao
interface HabitCompletionDao {
    @Query("SELECT * FROM habit_completions WHERE date = :date")
    fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions")
    fun getAllCompletions(): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY date DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE date BETWEEN :startDate AND :endDate")
    fun getCompletionsBetweenDates(startDate: String, endDate: String): Flow<List<HabitCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletionEntity): Long

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND date = :date")
    suspend fun deleteCompletion(habitId: Long, date: String)

    @Query("SELECT COUNT(*) FROM habit_completions WHERE habitId = :habitId AND date = :date")
    suspend fun isCompleted(habitId: Long, date: String): Int

    @Query("SELECT DISTINCT date FROM habit_completions WHERE habitId = :habitId ORDER BY date DESC")
    suspend fun getCompletedDatesForHabit(habitId: Long): List<String>

    @Query("SELECT DISTINCT date FROM habit_completions ORDER BY date DESC")
    suspend fun getAllUniqueCompletedDates(): List<String>

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsForHabit(habitId: Long)

    @Query("DELETE FROM habit_completions")
    suspend fun clearAllCompletions()
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE completedAt >= :startOfDayMs ORDER BY completedAt DESC")
    fun getTodaySessions(startOfDayMs: Long): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE isCompleted = 1")
    fun getTotalFocusMinutes(): Flow<Int>

    @Query("DELETE FROM focus_sessions")
    suspend fun clearAllSessions()
}

@Dao
interface QuoteDao {
    @Query("SELECT * FROM motivational_quotes ORDER BY id ASC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM motivational_quotes WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteQuotes(): Flow<List<QuoteEntity>>

    @Query("UPDATE motivational_quotes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Query("SELECT COUNT(*) FROM motivational_quotes")
    suspend fun getQuotesCount(): Int
}
