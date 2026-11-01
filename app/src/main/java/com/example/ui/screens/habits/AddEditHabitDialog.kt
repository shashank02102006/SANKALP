package com.example.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.HabitEntity
import com.example.ui.components.SankalpButton
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MutedGrey
import com.example.ui.theme.MutedMilitaryGreen
import com.example.ui.theme.OffWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditHabitDialog(
    initialHabit: HabitEntity? = null,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        category: String,
        frequency: String,
        customDays: String,
        reminderTime: String?,
        isReminderEnabled: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf(initialHabit?.title ?: "") }
    var description by remember { mutableStateOf(initialHabit?.description ?: "") }
    var category by remember { mutableStateOf(initialHabit?.category ?: "DISCIPLINE") }
    var frequency by remember { mutableStateOf(initialHabit?.frequency ?: "DAILY") }
    var reminderEnabled by remember { mutableStateOf(initialHabit?.isReminderEnabled ?: false) }
    var reminderHour by remember { mutableStateOf(initialHabit?.reminderTime?.split(":")?.getOrNull(0) ?: "07") }
    var reminderMinute by remember { mutableStateOf(initialHabit?.reminderTime?.split(":")?.getOrNull(1) ?: "00") }
    var titleError by remember { mutableStateOf(false) }

    val categories = listOf("DISCIPLINE", "PHYSICAL", "MIND", "STUDY", "NUTRITION", "ROUTINE")
    val frequencies = listOf("DAILY", "WEEKDAYS", "WEEKENDS")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkOlive),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialHabit == null) "NEW OBJECTIVE" else "EDIT OBJECTIVE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            letterSpacing = 1.sp
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MutedGrey
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title Input
                Text(
                    text = "HABIT NAME *",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    isError = titleError,
                    placeholder = { Text("e.g. 50 Pushups or Cold Shower", color = MutedGrey) },
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
                        .testTag("habit_title_input")
                )
                if (titleError) {
                    Text(
                        text = "Objective name is required",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Description Input
                Text(
                    text = "DESCRIPTION / MOTIVATION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Define your standard and non-negotiables", color = MutedGrey) },
                    maxLines = 3,
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
                        .testTag("habit_desc_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Chips
                Text(
                    text = "TACTICAL CATEGORY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GoldAccent else CharcoalBlack)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldBright else BorderOlive,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { category = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("category_chip_$cat")
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) CharcoalBlack else OffWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Frequency Chips
                Text(
                    text = "FREQUENCY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    frequencies.forEach { freq ->
                        val isSelected = frequency == freq
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) MutedMilitaryGreen else CharcoalBlack)
                                .border(
                                    1.dp,
                                    if (isSelected) MutedMilitaryGreen else BorderOlive,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { frequency = freq }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("freq_chip_$freq")
                        ) {
                            Text(
                                text = freq,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OffWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reminder Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DAILY REMINDER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OffWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Schedule tactical alarm notification",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MutedGrey,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CharcoalBlack,
                            checkedTrackColor = GoldAccent,
                            uncheckedThumbColor = MutedGrey,
                            uncheckedTrackColor = CharcoalBlack
                        ),
                        modifier = Modifier.testTag("reminder_switch")
                    )
                }

                if (reminderEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = reminderHour,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                    val num = it.toIntOrNull() ?: 0
                                    if (num in 0..23) reminderHour = it
                                }
                            },
                            label = { Text("Hour (0-23)", fontSize = 10.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CharcoalBlack,
                                unfocusedContainerColor = CharcoalBlack,
                                focusedTextColor = OffWhite,
                                unfocusedTextColor = OffWhite
                            ),
                            modifier = Modifier.width(110.dp)
                        )
                        Text(":", color = OffWhite, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = reminderMinute,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                    val num = it.toIntOrNull() ?: 0
                                    if (num in 0..59) reminderMinute = it
                                }
                            },
                            label = { Text("Min (0-59)", fontSize = 10.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CharcoalBlack,
                                unfocusedContainerColor = CharcoalBlack,
                                focusedTextColor = OffWhite,
                                unfocusedTextColor = OffWhite
                            ),
                            modifier = Modifier.width(110.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SankalpButton(
                        text = "CANCEL",
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.weight(1f),
                        testTag = "dialog_cancel_button"
                    )
                    SankalpButton(
                        text = if (initialHabit == null) "FORGE HABIT" else "UPDATE",
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                                return@SankalpButton
                            }
                            val timeStr = if (reminderEnabled) {
                                val h = reminderHour.padStart(2, '0')
                                val m = reminderMinute.padStart(2, '0')
                                "$h:$m"
                            } else null

                            onSave(
                                title.trim(),
                                description.trim(),
                                category,
                                frequency,
                                "MON,TUE,WED,THU,FRI,SAT,SUN",
                                timeStr,
                                reminderEnabled
                            )
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f),
                        testTag = "dialog_save_button"
                    )
                }
            }
        }
    }
}
