package com.example.ui.screens.focus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.SankalpButton
import com.example.ui.components.SankalpCard
import com.example.ui.components.SankalpTopBar
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MilitaryGreenBright
import com.example.ui.theme.MutedGrey
import com.example.ui.theme.MutedMilitaryGreen
import com.example.ui.theme.OffWhite
import com.example.ui.viewmodel.FocusViewModel
import com.example.ui.viewmodel.TimerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FocusScreen(
    viewModel: FocusViewModel,
    modifier: Modifier = Modifier
) {
    val timerState by viewModel.timerState.collectAsState()
    val selectedMinutes by viewModel.selectedMinutes.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val sessionTag by viewModel.sessionTag.collectAsState()
    val completedSessions by viewModel.completedSessions.collectAsState()
    val totalFocusMinutes by viewModel.totalFocusMinutes.collectAsState()

    var showCustomDialog by remember { mutableStateOf(false) }

    val totalSeconds = (selectedMinutes * 60).coerceAtLeast(1)
    val progressRatio = (remainingSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressRatio, label = "focus_progress")

    val minutesPart = remainingSeconds / 60
    val secondsPart = remainingSeconds % 60
    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutesPart, secondsPart)

    Scaffold(
        topBar = {
            SankalpTopBar(
                title = "TACTICAL FOCUS TIMER",
                subtitle = "UNBROKEN CONCENTRATION"
            )
        },
        containerColor = CharcoalBlack,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CharcoalBlack),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Preset Duration Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PresetButton(
                        label = "25 MIN",
                        isSelected = selectedMinutes == 25 && timerState == TimerState.STOPPED,
                        onClick = { viewModel.selectPreset(25) },
                        modifier = Modifier.weight(1f)
                    )
                    PresetButton(
                        label = "45 MIN",
                        isSelected = selectedMinutes == 45 && timerState == TimerState.STOPPED,
                        onClick = { viewModel.selectPreset(45) },
                        modifier = Modifier.weight(1f)
                    )
                    PresetButton(
                        label = "CUSTOM",
                        isSelected = (selectedMinutes != 25 && selectedMinutes != 45) && timerState == TimerState.STOPPED,
                        onClick = { showCustomDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Big Circular Countdown Display
            item {
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Background track
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(220.dp),
                        color = DarkOlive,
                        strokeWidth = 12.dp,
                        strokeCap = StrokeCap.Round
                    )
                    // Active countdown arc
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(220.dp),
                        color = if (timerState == TimerState.RUNNING) GoldAccent else MutedMilitaryGreen,
                        strokeWidth = 12.dp,
                        strokeCap = StrokeCap.Round
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 44.sp,
                                color = if (timerState == TimerState.RUNNING) GoldBright else OffWhite,
                                letterSpacing = 2.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (timerState) {
                                TimerState.RUNNING -> "ACTIVE DRILL"
                                TimerState.PAUSED -> "PAUSED"
                                TimerState.COMPLETED -> "VICTORY ACHIEVED"
                                TimerState.STOPPED -> "READY FOR COMBAT"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (timerState == TimerState.RUNNING) GoldAccent else MutedGrey,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Timer Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (timerState) {
                        TimerState.STOPPED, TimerState.COMPLETED -> {
                            SankalpButton(
                                text = "COMMENCE DRILL",
                                onClick = { viewModel.startTimer() },
                                modifier = Modifier.fillMaxWidth(),
                                isPrimary = true,
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = CharcoalBlack
                                    )
                                },
                                testTag = "start_timer_button"
                            )
                        }
                        TimerState.RUNNING -> {
                            SankalpButton(
                                text = "HOLD (PAUSE)",
                                onClick = { viewModel.pauseTimer() },
                                modifier = Modifier.weight(1f),
                                isPrimary = false,
                                testTag = "pause_timer_button"
                            )
                            SankalpButton(
                                text = "ABORT (RESET)",
                                onClick = { viewModel.resetTimer() },
                                modifier = Modifier.weight(1f),
                                isPrimary = true,
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = CharcoalBlack
                                    )
                                },
                                testTag = "reset_timer_button"
                            )
                        }
                        TimerState.PAUSED -> {
                            SankalpButton(
                                text = "RESUME",
                                onClick = { viewModel.resumeTimer() },
                                modifier = Modifier.weight(1f),
                                isPrimary = true,
                                testTag = "resume_timer_button"
                            )
                            SankalpButton(
                                text = "RESET",
                                onClick = { viewModel.resetTimer() },
                                modifier = Modifier.weight(1f),
                                isPrimary = false,
                                testTag = "reset_timer_button"
                            )
                        }
                    }
                }
            }

            // Stats Strip
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SankalpCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$totalFocusMinutes",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "TOTAL MINUTES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MutedGrey,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    SankalpCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${completedSessions.size}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MilitaryGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "SESSIONS FORGED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MutedGrey,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            // Session History Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COMPLETED FOCUS DRILLS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = OffWhite,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "${completedSessions.size} Logged",
                        style = MaterialTheme.typography.labelSmall.copy(color = GoldAccent)
                    )
                }
            }

            // Session History List
            if (completedSessions.isEmpty()) {
                item {
                    SankalpCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = MutedMilitaryGreen,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "NO FOCUS SESSIONS YET",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete a focus drill to forge deep concentration records.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MutedGrey,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            } else {
                items(completedSessions.take(15)) { session ->
                    val sdf = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
                    val dateFormatted = remember(session.completedAt) {
                        sdf.format(Date(session.completedAt))
                    }

                    SankalpCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = session.tag,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = OffWhite,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = dateFormatted,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MutedGrey,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(DarkOlive, RoundedCornerShape(6.dp))
                                    .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+${session.durationMinutes} MIN",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Custom Duration Dialog
    if (showCustomDialog) {
        var inputMinutes by remember { mutableStateOf(selectedMinutes.toString()) }
        var inputTag by remember { mutableStateOf(sessionTag) }

        Dialog(onDismissRequest = { showCustomDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkOlive),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CUSTOM FOCUS DURATION",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "DURATION (MINUTES 1 - 180)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedGrey,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = inputMinutes,
                        onValueChange = { inputMinutes = it.filter { c -> c.isDigit() }.take(3) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CharcoalBlack,
                            unfocusedContainerColor = CharcoalBlack,
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = BorderOlive
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_minutes_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "MISSION OBJECTIVE TAG",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedGrey,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = inputTag,
                        onValueChange = { inputTag = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CharcoalBlack,
                            unfocusedContainerColor = CharcoalBlack,
                            focusedTextColor = OffWhite,
                            unfocusedTextColor = OffWhite,
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = BorderOlive
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SankalpButton(
                            text = "CANCEL",
                            onClick = { showCustomDialog = false },
                            isPrimary = false,
                            modifier = Modifier.weight(1f)
                        )
                        SankalpButton(
                            text = "SET DRILL",
                            onClick = {
                                val min = inputMinutes.toIntOrNull() ?: 25
                                viewModel.setCustomMinutes(min)
                                if (inputTag.isNotBlank()) {
                                    viewModel.setSessionTag(inputTag.trim())
                                }
                                showCustomDialog = false
                            },
                            isPrimary = true,
                            modifier = Modifier.weight(1f),
                            testTag = "save_custom_minutes_button"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PresetButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GoldAccent else DarkOlive)
            .border(
                1.dp,
                if (isSelected) GoldBright else BorderOlive,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) CharcoalBlack else OffWhite,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
    }
}
