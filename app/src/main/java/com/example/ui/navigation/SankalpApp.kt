package com.example.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.focus.FocusScreen
import com.example.ui.screens.habits.HabitsScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.welcome.WelcomeScreen
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MutedGrey
import com.example.ui.viewmodel.FocusViewModel
import com.example.ui.viewmodel.HabitViewModel

enum class NavigationDestination(
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Default.Dashboard, "nav_item_home"),
    HABITS("Habits", Icons.Default.FormatListBulleted, "nav_item_habits"),
    PROGRESS("Progress", Icons.Default.Insights, "nav_item_progress"),
    FOCUS("Focus", Icons.Default.Timer, "nav_item_focus"),
    SETTINGS("Settings", Icons.Default.Settings, "nav_item_settings")
}

@Composable
fun SankalpApp(
    habitViewModel: HabitViewModel = viewModel(),
    focusViewModel: FocusViewModel = viewModel()
) {
    val onboardingCompleted by habitViewModel.onboardingCompleted.collectAsState()
    var currentDestination by remember { mutableStateOf(NavigationDestination.HOME) }

    if (!onboardingCompleted) {
        WelcomeScreen(
            onBeginMission = { name ->
                habitViewModel.completeOnboarding(name)
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = DarkOlive,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(0.5.dp, BorderOlive)
                ) {
                    NavigationDestination.values().forEach { destination ->
                        val isSelected = currentDestination == destination
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentDestination = destination },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = destination.title.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GoldAccent,
                                selectedTextColor = GoldAccent,
                                unselectedIconColor = MutedGrey,
                                unselectedTextColor = MutedGrey,
                                indicatorColor = CharcoalBlack
                            ),
                            modifier = Modifier.testTag(destination.testTag)
                        )
                    }
                }
            },
            containerColor = CharcoalBlack
        ) { innerPadding ->
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "nav_transition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { target ->
                when (target) {
                    NavigationDestination.HOME -> DashboardScreen(viewModel = habitViewModel)
                    NavigationDestination.HABITS -> HabitsScreen(viewModel = habitViewModel)
                    NavigationDestination.PROGRESS -> ProgressScreen(viewModel = habitViewModel)
                    NavigationDestination.FOCUS -> FocusScreen(viewModel = focusViewModel)
                    NavigationDestination.SETTINGS -> SettingsScreen(viewModel = habitViewModel)
                }
            }
        }
    }
}
