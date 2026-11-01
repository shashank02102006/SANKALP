package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.HabitRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SANKALP", appName)
    }

    @Test
    fun `test habit streak calculation`() {
        val today = LocalDate.of(2026, 11, 1)
        val completedDates = setOf(
            "2026-11-01",
            "2026-10-31",
            "2026-10-30",
            "2026-10-29"
        )
        val (currentStreak, longestStreak) = HabitRepository.calculateStreaks(completedDates, today)
        assertEquals(4, currentStreak)
        assertEquals(4, longestStreak)
    }
}
