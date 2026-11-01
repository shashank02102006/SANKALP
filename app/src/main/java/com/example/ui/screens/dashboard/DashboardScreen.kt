package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.HabitEntity
import com.example.ui.components.CircularProgressDisplay
import com.example.ui.components.HabitItemCard
import com.example.ui.components.QuoteCard
import com.example.ui.components.SankalpButton
import com.example.ui.components.SankalpCard
import com.example.ui.components.SankalpTopBar
import com.example.ui.screens.habits.AddEditHabitDialog
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MilitaryGreenBright
import com.example.ui.theme.MutedGrey
import com.example.ui.theme.MutedMilitaryGreen
import com.example.ui.theme.OffWhite
import com.example.ui.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val todayHabits by viewModel.todayHabits.collectAsState()
    val globalStreaks by viewModel.globalStreaks.collectAsState()
    val dailyQuote by viewModel.dailyQuote.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }

    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")
    val formattedDate = today.format(dateFormatter).uppercase()

    val totalHabits = todayHabits.size
    val completedCount = todayHabits.count { it.isCompletedToday }
    val percentage = if (totalHabits > 0) ((completedCount.toFloat() / totalHabits) * 100).toInt() else 0

    Scaffold(
        topBar = {
            SankalpTopBar(
                title = "HQ DASHBOARD",
                subtitle = "OPERATIONAL READINESS"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GoldAccent,
                contentColor = CharcoalBlack,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("fab_add_habit")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Habit")
            }
        },
        containerColor = CharcoalBlack,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CharcoalBlack),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting & Date Banner
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "GREETINGS, $userName".uppercase(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = OffWhite,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MutedGrey,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        // Streak Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(DarkOlive, RoundedCornerShape(20.dp))
                                .border(1.dp, GoldAccent, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("streak_pill")
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Current Streak",
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${globalStreaks.first} DAYS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = GoldBright,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // Daily Progress Overview Card
            item {
                SankalpCard(
                    borderColor = if (percentage == 100) GoldAccent else BorderOlive,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "TODAY'S MISSION PROGRESS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (totalHabits == 0) "No Active Objectives"
                                else if (percentage == 100) "ALL OBJECTIVES SECURED"
                                else "$completedCount of $totalHabits Completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OffWhite
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Linear progress indicator
                            LinearProgressIndicator(
                                progress = { (percentage / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (percentage == 100) GoldAccent else MilitaryGreenBright,
                                trackColor = CharcoalBlack,
                                strokeCap = StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Longest Streak: ${globalStreaks.second} Days",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MutedGrey,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        CircularProgressDisplay(
                            percentage = percentage,
                            completed = completedCount,
                            total = totalHabits,
                            size = 96
                        )
                    }
                }
            }

            // Daily Motivational Quote
            item {
                QuoteCard(
                    quote = dailyQuote,
                    onToggleFavorite = { fav ->
                        viewModel.toggleFavoriteQuote(dailyQuote.id, dailyQuote.isFavorite)
                    }
                )
            }

            // Checklist Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S HABIT CHECKLIST",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OffWhite,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "$completedCount/$totalHabits",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Habits List
            if (todayHabits.isEmpty()) {
                item {
                    SankalpCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = MutedMilitaryGreen,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "NO HABITS DEFINED",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap 'Add Objective' to forge your first daily discipline habit.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MutedGrey,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            SankalpButton(
                                text = "ADD OBJECTIVE",
                                onClick = { showAddDialog = true },
                                testTag = "empty_add_habit_button"
                            )
                        }
                    }
                }
            } else {
                items(todayHabits, key = { it.habit.id }) { item ->
                    HabitItemCard(
                        habitWithStatus = item,
                        onToggle = { viewModel.toggleHabitCompletion(item.habit.id, today) },
                        onEdit = { editingHabit = item.habit },
                        onDelete = { viewModel.deleteHabit(item.habit) }
                    )
                }
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }

    if (showAddDialog) {
        AddEditHabitDialog(
            initialHabit = null,
            onDismiss = { showAddDialog = false },
            onSave = { title, desc, cat, freq, days, time, rem ->
                viewModel.createHabit(title, desc, cat, freq, days, time, rem)
                showAddDialog = false
            }
        )
    }

    if (editingHabit != null) {
        AddEditHabitDialog(
            initialHabit = editingHabit,
            onDismiss = { editingHabit = null },
            onSave = { title, desc, cat, freq, days, time, rem ->
                editingHabit?.let { orig ->
                    viewModel.updateHabit(
                        orig.copy(
                            title = title,
                            description = desc,
                            category = cat,
                            frequency = freq,
                            customDays = days,
                            reminderTime = time,
                            isReminderEnabled = rem
                        )
                    )
                }
                editingHabit = null
            }
        )
    }
}
