package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.FocusSessionEntity
import com.example.data.repository.HabitRepository
import com.example.notification.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TimerState {
    STOPPED,
    RUNNING,
    PAUSED,
    COMPLETED
}

class FocusViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repository = HabitRepository(db)
    private val notificationHelper = NotificationHelper(application)

    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _selectedMinutes = MutableStateFlow(25)
    val selectedMinutes: StateFlow<Int> = _selectedMinutes.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(25 * 60)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _sessionTag = MutableStateFlow("Tactical Concentration")
    val sessionTag: StateFlow<String> = _sessionTag.asStateFlow()

    private var timerJob: Job? = null

    val completedSessions: StateFlow<List<FocusSessionEntity>> = repository.allFocusSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusMinutes: StateFlow<Int> = repository.totalFocusMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectPreset(minutes: Int) {
        if (_timerState.value == TimerState.RUNNING) return
        _selectedMinutes.value = minutes
        _remainingSeconds.value = minutes * 60
        _timerState.value = TimerState.STOPPED
    }

    fun setCustomMinutes(minutes: Int) {
        val valid = minutes.coerceIn(1, 180)
        _selectedMinutes.value = valid
        _remainingSeconds.value = valid * 60
        _timerState.value = TimerState.STOPPED
    }

    fun setSessionTag(tag: String) {
        _sessionTag.value = tag
    }

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return

        if (_timerState.value == TimerState.STOPPED || _timerState.value == TimerState.COMPLETED) {
            _remainingSeconds.value = _selectedMinutes.value * 60
        }

        _timerState.value = TimerState.RUNNING
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0) {
                delay(1000L)
                _remainingSeconds.value -= 1
            }
            onTimerFinished()
        }
    }

    fun pauseTimer() {
        if (_timerState.value != TimerState.RUNNING) return
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun resumeTimer() {
        if (_timerState.value != TimerState.PAUSED) return
        startTimer()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _remainingSeconds.value = _selectedMinutes.value * 60
        _timerState.value = TimerState.STOPPED
    }

    private fun onTimerFinished() {
        _timerState.value = TimerState.COMPLETED
        val minutes = _selectedMinutes.value
        val tag = _sessionTag.value

        // Trigger notification
        notificationHelper.showFocusCompletedNotification(minutes)

        // Haptic pulse
        triggerVibration()

        // Persist session to Room
        viewModelScope.launch {
            repository.saveFocusSession(minutes, tag)
        }
    }

    private fun triggerVibration() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val effect = VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1)
                vibratorManager?.vibrate(CombinedVibration.createParallel(effect))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(500)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
