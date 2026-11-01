package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.FocusSessionEntity
import com.example.data.model.HabitCompletionEntity
import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class,
        FocusSessionEntity::class,
        QuoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sankalp_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default military-grade habits and quotes asynchronously
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.habitDao().insertHabits(DefaultData.defaultHabits)
                                database.quoteDao().insertQuotes(DefaultData.motivationalQuotes)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
