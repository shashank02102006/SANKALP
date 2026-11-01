package com.example.ui.screens.habits

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.HabitEntity
import com.example.data.repository.HabitWithStatus
import com.example.ui.components.CategoryBadge
import com.example.ui.components.HabitItemCard
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
import com.example.ui.viewmodel.HabitViewModel
import java.time.LocalDate

@Composable
fun HabitsScreen(
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    val habitsWithStatus by viewModel.habitsForSelectedDate.collectAsState()
    val today = LocalDate.now()

    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }
    var inspectingHabit by remember { mutableStateOf<HabitWithStatus?>(null) }

    val filterOptions = listOf("ALL", "DISCIPLINE", "PHYSICAL", "MIND", "STUDY", "NUTRITION", "ROUTINE")

    val filteredList = habitsWithStatus.filter {
        if (selectedCategoryFilter == "ALL") true
        else it.habit.category.equals(selectedCategoryFilter, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            SankalpTopBar(
                title = "HABIT ARSENAL",
                subtitle = "TACTICAL DRILLS & OBJECTIVES"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GoldAccent,
                contentColor = CharcoalBlack,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("habits_add_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Habit")
            }
        },
        containerColor = CharcoalBlack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CharcoalBlack)
        ) {
            // Category Filter Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedCategoryFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) GoldAccent else DarkOlive)
                            .border(
                                1.dp,
                                if (isSelected) GoldBright else BorderOlive,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategoryFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("filter_chip_$filter")
                    ) {
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) CharcoalBlack else OffWhite,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }

            // Stats Summary Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredList.size} OBJECTIVES REGISTERED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "${filteredList.count { it.isCompletedToday }} COMPLETED TODAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Habit List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                if (filteredList.isEmpty()) {
                    item {
                        SankalpCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    tint = MutedMilitaryGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "NO HABITS IN THIS CATEGORY",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = OffWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Create a new discipline objective or switch filter to ALL.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MutedGrey
                                    )
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.habit.id }) { item ->
                        Box(modifier = Modifier.clickable { inspectingHabit = item }) {
                            HabitItemCard(
                                habitWithStatus = item,
                                onToggle = { viewModel.toggleHabitCompletion(item.habit.id, today) },
                                onEdit = { editingHabit = item.habit },
                                onDelete = { viewModel.deleteHabit(item.habit) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Dialog
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

    // Edit Dialog
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

    // Habit Details Inspection Dialog
    if (inspectingHabit != null) {
        val habit = inspectingHabit!!.habit
        Dialog(onDismissRequest = { inspectingHabit = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkOlive),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OBJECTIVE DOSSIER",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.sp
                            )
                        )
                        IconButton(onClick = { inspectingHabit = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MutedGrey)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OffWhite
                        )
                    )

                    if (habit.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = habit.description,
                            style = MaterialTheme.typography.bodyLarge.copy(color = MutedGrey)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryBadge(category = habit.category)
                        Box(
                            modifier = Modifier
                                .background(CharcoalBlack, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FREQ: ${habit.frequency}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OffWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SankalpCard(
                            borderColor = BorderOlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${inspectingHabit!!.currentStreak}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Current Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MutedGrey, fontSize = 10.sp)
                                )
                            }
                        }

                        SankalpCard(
                            borderColor = BorderOlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${inspectingHabit!!.longestStreak}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MilitaryGreenBright,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Best Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MutedGrey, fontSize = 10.sp)
                                )
                            }
                        }

                        SankalpCard(
                            borderColor = BorderOlive,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${inspectingHabit!!.totalCompletions}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = OffWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Total Days",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MutedGrey, fontSize = 10.sp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    SankalpButton(
                        text = if (inspectingHabit!!.isCompletedToday) "UNDO COMPLETION" else "MARK COMPLETED TODAY",
                        onClick = {
                            viewModel.toggleHabitCompletion(habit.id, today)
                            inspectingHabit = null
                        },
                        isPrimary = !inspectingHabit!!.isCompletedToday,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
