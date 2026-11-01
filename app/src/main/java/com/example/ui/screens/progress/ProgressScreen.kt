package com.example.ui.screens.progress

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.DayCompletionSummary
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun ProgressScreen(
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("WEEKLY ANALYTICS", "MONTHLY CALENDAR")

    Scaffold(
        topBar = {
            SankalpTopBar(
                title = "PROGRESS & TELEMETRY",
                subtitle = "DISCIPLINE CONSISTENCY"
            )
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
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkOlive,
                contentColor = GoldAccent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GoldAccent,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = if (selectedTab == index) GoldAccent else MutedGrey
                                )
                            )
                        },
                        modifier = Modifier.testTag("tab_$index")
                    )
                }
            }

            if (selectedTab == 0) {
                WeeklyAnalyticsView(viewModel = viewModel)
            } else {
                MonthlyCalendarView(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WeeklyAnalyticsView(viewModel: HabitViewModel) {
    val weeklyAnalytics by viewModel.weeklyAnalytics.collectAsState()
    val globalStreaks by viewModel.globalStreaks.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Metrics Cards
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
                            text = "${weeklyAnalytics.averagePercentage}%",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "7-DAY AVERAGE",
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
                            text = "${weeklyAnalytics.totalCompletionsThisWeek}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MilitaryGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "COMPLETIONS",
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
                            text = "${globalStreaks.first}d",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = OffWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "CURRENT STREAK",
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

        // Weekly Progress Bar Chart
        item {
            SankalpCard(
                borderColor = GoldAccent.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LAST 7 DAYS EXECUTION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Bars Container
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklyAnalytics.days.forEach { day ->
                            WeeklyChartColumn(day = day)
                        }
                    }
                }
            }
        }

        // Best Day Highlight
        item {
            if (weeklyAnalytics.bestDay != null && weeklyAnalytics.bestDay!!.percentage > 0) {
                SankalpCard(
                    borderColor = GoldAccent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = CharcoalBlack,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "TOP OPERATIONAL PERFORMANCE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "${weeklyAnalytics.bestDay!!.date.dayOfWeek.name} (${weeklyAnalytics.bestDay!!.percentage}% Execution)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${weeklyAnalytics.bestDay!!.completedCount} objectives executed flawlessly.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MutedGrey,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Daily Detailed List
        item {
            Text(
                text = "DAILY MISSION BREAKDOWN",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = OffWhite,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        items(weeklyAnalytics.days.reversed()) { day ->
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
                            text = "${day.date.dayOfWeek.name}, ${day.date.dayOfMonth} ${day.date.month.name.take(3)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = OffWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${day.completedCount} of ${day.totalHabits} habits executed",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MutedGrey,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                when {
                                    day.percentage == 100 -> GoldAccent
                                    day.percentage >= 50 -> MutedMilitaryGreen
                                    day.percentage > 0 -> DarkOlive
                                    else -> CharcoalBlack
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, BorderOlive, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${day.percentage}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (day.percentage == 100) CharcoalBlack else OffWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyChartColumn(day: DayCompletionSummary) {
    val barHeightRatio = (day.percentage / 100f).coerceIn(0.08f, 1f)
    val barColor = when {
        day.percentage == 100 -> GoldAccent
        day.percentage >= 50 -> MilitaryGreenBright
        day.percentage > 0 -> MutedMilitaryGreen
        else -> BorderOlive
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(36.dp)
    ) {
        Text(
            text = "${day.percentage}%",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (day.percentage > 0) GoldAccent else MutedGrey
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Bar
        Box(
            modifier = Modifier
                .width(16.dp)
                .height((100 * barHeightRatio).dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(barColor)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = day.dayLabel,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = OffWhite,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "${day.date.dayOfMonth}",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                color = MutedGrey
            )
        )
    }
}

@Composable
fun MonthlyCalendarView(viewModel: HabitViewModel) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val allCompletions by viewModel.allCompletions.collectAsState()
    val activeHabits by viewModel.activeHabits.collectAsState()

    val totalHabits = activeHabits.size.coerceAtLeast(1)
    val completionsByDate = remember(allCompletions) {
        allCompletions.groupBy { it.date }
    }

    // Days in current month
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value // 1 = Monday, 7 = Sunday
    val leadSpaces = firstDayOfMonth - 1

    val habitsCompletedOnSelectedDate = remember(selectedDate, allCompletions, activeHabits) {
        val selectedDateStr = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val completedHabitIds = completionsByDate[selectedDateStr]?.map { it.habitId }?.toSet() ?: emptySet()
        activeHabits.map { habit ->
            Pair(habit, completedHabitIds.contains(habit.id))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Month Navigation Header
        item {
            SankalpCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { currentMonth = currentMonth.minusMonths(1) },
                        modifier = Modifier.testTag("prev_month_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = GoldAccent
                        )
                    }

                    Text(
                        text = "${currentMonth.month.name} ${currentMonth.year}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OffWhite,
                            letterSpacing = 1.sp
                        )
                    )

                    IconButton(
                        onClick = { currentMonth = currentMonth.plusMonths(1) },
                        modifier = Modifier.testTag("next_month_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = GoldAccent
                        )
                    }
                }
            }
        }

        // Calendar Day of Week Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedGrey,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Calendar Grid
        item {
            val totalCells = leadSpaces + daysInMonth
            val rows = (totalCells + 6) / 7

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (c in 0 until 7) {
                            val cellIndex = r * 7 + c
                            val dayNumber = cellIndex - leadSpaces + 1

                            if (cellIndex < leadSpaces || dayNumber > daysInMonth) {
                                Box(modifier = Modifier.size(42.dp))
                            } else {
                                val date = currentMonth.atDay(dayNumber)
                                val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                                val completedOnDate = completionsByDate[dateStr]?.map { it.habitId }?.distinct()?.size ?: 0
                                val isSelected = date == selectedDate
                                val isToday = date == LocalDate.now()
                                val isFuture = date.isAfter(LocalDate.now())

                                val statusColor = when {
                                    isFuture -> Color.Transparent
                                    completedOnDate >= totalHabits -> GoldAccent
                                    completedOnDate > 0 -> MutedMilitaryGreen
                                    else -> Color.Transparent
                                }

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) GoldAccent.copy(alpha = 0.35f)
                                            else if (completedOnDate >= totalHabits) DarkOlive
                                            else CharcoalBlack
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else if (isToday) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) GoldAccent else if (isToday) MilitaryGreenBright else BorderOlive,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedDate = date }
                                        .testTag("calendar_day_$dayNumber"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "$dayNumber",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) GoldBright else if (isFuture) MutedGrey.copy(alpha = 0.5f) else OffWhite,
                                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        )
                                        if (statusColor != Color.Transparent) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(statusColor)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Date Log Header
        item {
            val selectedDateFormatted = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
            Text(
                text = "LOG FOR ${selectedDateFormatted.uppercase()}",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        // Habit completed list on selected date
        if (habitsCompletedOnSelectedDate.isEmpty()) {
            item {
                Text("No habits configured.", color = MutedGrey, fontSize = 12.sp)
            }
        } else {
            items(habitsCompletedOnSelectedDate) { (habit, isDone) ->
                SankalpCard(
                    borderColor = if (isDone) GoldAccent.copy(alpha = 0.5f) else BorderOlive,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = habit.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "Category: ${habit.category} • ${habit.frequency}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MutedGrey,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDone) GoldAccent else CharcoalBlack)
                                .border(1.dp, if (isDone) GoldAccent else BorderOlive, RoundedCornerShape(6.dp))
                                .clickable {
                                    viewModel.toggleHabitCompletion(habit.id, selectedDate)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("toggle_date_habit_${habit.id}")
                        ) {
                            Text(
                                text = if (isDone) "COMPLETED" else "MARK DONE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isDone) CharcoalBlack else OffWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
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
