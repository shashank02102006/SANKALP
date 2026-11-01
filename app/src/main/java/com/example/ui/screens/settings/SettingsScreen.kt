package com.example.ui.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.QuoteEntity
import com.example.ui.components.SankalpButton
import com.example.ui.components.SankalpCard
import com.example.ui.components.SankalpTopBar
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MilitaryGreenBright
import com.example.ui.theme.MutedGrey
import com.example.ui.theme.MutedMilitaryGreen
import com.example.ui.theme.OffWhite
import com.example.ui.viewmodel.HabitViewModel

@Composable
fun SettingsScreen(
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val favoriteQuotes by viewModel.favoriteQuotes.collectAsState()

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var showFavoritesDialog by remember { mutableStateOf(false) }

    // Android 13+ Notification Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.toggleNotifications(isGranted)
    }

    Scaffold(
        topBar = {
            SankalpTopBar(
                title = "SETTINGS & PROFILE",
                subtitle = "COMMAND PROTOCOLS"
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                SankalpCard(
                    borderColor = GoldAccent.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, GoldAccent, CircleShape)
                                    .background(CharcoalBlack),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.sankalp_crest),
                                    contentDescription = "Crest",
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = userName.uppercase(),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = OffWhite
                                    )
                                )
                                Text(
                                    text = "RANK: DISCIPLINED COMMANDER",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditNameDialog = true },
                            modifier = Modifier.testTag("edit_name_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Name",
                                tint = GoldAccent
                            )
                        }
                    }
                }
            }

            // Notification Preferences
            item {
                SankalpCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "DAILY MISSION NOTIFICATIONS",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = OffWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Alarms and reminders for discipline drills",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MutedGrey,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { enable ->
                                    if (enable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.toggleNotifications(enable)
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CharcoalBlack,
                                    checkedTrackColor = GoldAccent,
                                    uncheckedThumbColor = MutedGrey,
                                    uncheckedTrackColor = CharcoalBlack
                                ),
                                modifier = Modifier.testTag("settings_notification_switch")
                            )
                        }
                    }
                }
            }

            // Saved Quotes Section
            item {
                SankalpCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFavoritesDialog = true }
                        .testTag("favorite_quotes_row")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "SAVED MOTIVATIONAL QUOTES",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = OffWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${favoriteQuotes.size} quotes marked as favorite",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MutedGrey,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "VIEW",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // About SANKALP Card
            item {
                SankalpCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MutedMilitaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "ABOUT SANKALP",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "SANKALP is engineered for individuals committed to unbreakable personal discipline, physical excellence, and mental fortitude.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MutedGrey,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "VERSION",
                                style = MaterialTheme.typography.labelSmall.copy(color = MutedGrey)
                            )
                            Text(
                                text = "1.0.0 (MIL-SPEC TACTICAL)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "DATA ARCHITECTURE",
                                style = MaterialTheme.typography.labelSmall.copy(color = MutedGrey)
                            )
                            Text(
                                text = "100% OFFLINE LOCAL ROOM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MilitaryGreenBright,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            // Reset Data Button
            item {
                SankalpButton(
                    text = "RESET ALL APPLICATION DATA",
                    onClick = { showResetConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = ErrorRed
                        )
                    },
                    testTag = "reset_data_button"
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Edit Name Dialog
    if (showEditNameDialog) {
        var newName by remember { mutableStateOf(userName) }
        Dialog(onDismissRequest = { showEditNameDialog = false }) {
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
                        text = "EDIT CALLSIGN / NAME",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
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
                            .testTag("edit_name_input")
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SankalpButton(
                            text = "CANCEL",
                            onClick = { showEditNameDialog = false },
                            isPrimary = false,
                            modifier = Modifier.weight(1f)
                        )
                        SankalpButton(
                            text = "SAVE",
                            onClick = {
                                viewModel.updateUserName(newName.trim().ifEmpty { "Commander" })
                                showEditNameDialog = false
                            },
                            isPrimary = true,
                            modifier = Modifier.weight(1f),
                            testTag = "save_name_button"
                        )
                    }
                }
            }
        }
    }

    // Reset Data Confirmation Dialog
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = {
                Text(
                    text = "CONFIRM DATA PURGE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                )
            },
            text = {
                Text(
                    text = "This action will permanently delete all habit completion history, focus sessions, and reset to default military habits. Are you certain you wish to proceed?",
                    style = MaterialTheme.typography.bodyMedium.copy(color = OffWhite)
                )
            },
            confirmButton = {
                SankalpButton(
                    text = "PURGE ALL",
                    onClick = {
                        viewModel.resetAllData()
                        showResetConfirmDialog = false
                    },
                    isPrimary = true,
                    testTag = "confirm_purge_button"
                )
            },
            dismissButton = {
                SankalpButton(
                    text = "CANCEL",
                    onClick = { showResetConfirmDialog = false },
                    isPrimary = false
                )
            },
            containerColor = DarkOlive,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Favorite Quotes Dialog
    if (showFavoritesDialog) {
        Dialog(onDismissRequest = { showFavoritesDialog = false }) {
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
                            text = "FAVORITE MOTIVATIONAL QUOTES",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        )
                        IconButton(onClick = { showFavoritesDialog = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MutedGrey)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (favoriteQuotes.isEmpty()) {
                        Text(
                            text = "No quotes saved as favorites yet. Tap the bookmark/heart on any daily quote to save it.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MutedGrey)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.height(300.dp)
                        ) {
                            items(favoriteQuotes) { quote ->
                                SankalpCard(
                                    borderColor = BorderOlive,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "“${quote.quote}”",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = OffWhite,
                                                fontSize = 12.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "— ${quote.author.uppercase()}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = GoldAccent,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    SankalpButton(
                        text = "CLOSE",
                        onClick = { showFavoritesDialog = false },
                        isPrimary = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
