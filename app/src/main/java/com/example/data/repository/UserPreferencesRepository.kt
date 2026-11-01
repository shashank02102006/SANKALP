package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("sankalp_prefs", Context.MODE_PRIVATE)

    private val _userName = MutableStateFlow(prefs.getString("user_name", "Commander") ?: "Commander")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _onboardingCompleted =
        MutableStateFlow(prefs.getBoolean("onboarding_completed", false))
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _notificationsEnabled =
        MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _dailyBriefingTime =
        MutableStateFlow(prefs.getString("daily_briefing_time", "07:00") ?: "07:00")
    val dailyBriefingTime: StateFlow<String> = _dailyBriefingTime.asStateFlow()

    fun setUserName(name: String) {
        val trimmed = name.trim().ifEmpty { "Commander" }
        prefs.edit().putString("user_name", trimmed).apply()
        _userName.value = trimmed
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
        _onboardingCompleted.value = completed
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
        _notificationsEnabled.value = enabled
    }

    fun setDailyBriefingTime(time: String) {
        prefs.edit().putString("daily_briefing_time", time).apply()
        _dailyBriefingTime.value = time
    }

    fun resetPreferences() {
        prefs.edit().clear().apply()
        _userName.value = "Commander"
        _onboardingCompleted.value = false
        _notificationsEnabled.value = true
        _dailyBriefingTime.value = "07:00"
    }
}
