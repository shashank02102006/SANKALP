package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity
import com.example.data.repository.HabitWithStatus
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
import com.example.ui.theme.SuccessGreen

@Composable
fun SankalpTopBar(
    title: String,
    subtitle: String? = null,
    actions: @Composable (() -> Unit)? = null
) {
    Surface(
        color = CharcoalBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = OffWhite
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldAccent,
                            letterSpacing = 1.2.sp
                        )
                    )
                }
            }
            if (actions != null) {
                actions()
            }
        }
    }
}

@Composable
fun SankalpButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "sankalp_button"
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldAccent,
                contentColor = CharcoalBlack,
                disabledContainerColor = DarkOlive,
                disabledContentColor = MutedGrey
            ),
            modifier = modifier
                .height(48.dp)
                .testTag(testTag)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MutedMilitaryGreen),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = OffWhite,
                disabledContentColor = MutedGrey
            ),
            modifier = modifier
                .height(48.dp)
                .testTag(testTag)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun SankalpCard(
    modifier: Modifier = Modifier,
    borderColor: Color = BorderOlive,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkOlive
        ),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun CircularProgressDisplay(
    percentage: Int,
    completed: Int,
    total: Int,
    modifier: Modifier = Modifier,
    size: Int = 110
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (percentage / 100f).coerceIn(0f, 1f),
        label = "progress_anim"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size.dp)
    ) {
        // Track background
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(size.dp),
            color = BorderOlive,
            strokeWidth = 9.dp,
            strokeCap = StrokeCap.Round,
        )
        // Active progress arc
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(size.dp),
            color = if (percentage == 100) GoldAccent else MilitaryGreenBright,
            strokeWidth = 9.dp,
            strokeCap = StrokeCap.Round,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (percentage == 100) GoldBright else OffWhite,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = "$completed/$total",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MutedGrey,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun HabitItemCard(
    habitWithStatus: HabitWithStatus,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habit = habitWithStatus.habit
    val isCompleted = habitWithStatus.isCompletedToday

    val cardBorderColor by animateColorAsState(
        targetValue = if (isCompleted) GoldAccent.copy(alpha = 0.6f) else BorderOlive,
        label = "card_border"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) DarkOlive.copy(alpha = 0.85f) else DarkOlive
        ),
        border = BorderStroke(1.dp, cardBorderColor),
        modifier = modifier
            .fillMaxWidth()
            .testTag("habit_card_${habit.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tactile Custom Checkbox
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) GoldAccent else CharcoalBlack)
                    .border(
                        width = 1.5.dp,
                        color = if (isCompleted) GoldAccent else MutedMilitaryGreen,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle
                    )
                    .testTag("habit_checkbox_${habit.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = CharcoalBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Habit Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCompleted) MutedGrey else OffWhite,
                            textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (habit.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = habit.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MutedGrey,
                            fontSize = 12.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Badges Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryBadge(category = habit.category)

                    if (habitWithStatus.currentStreak > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(CharcoalBlack, RoundedCornerShape(4.dp))
                                .border(0.5.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = GoldAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${habitWithStatus.currentStreak}d",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldBright,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    if (habit.isReminderEnabled && habit.reminderTime != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(CharcoalBlack, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminder",
                                tint = MutedMilitaryGreen,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = habit.reminderTime,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MutedMilitaryGreen,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            // Quick actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("edit_habit_${habit.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Habit",
                        tint = MutedGrey,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("delete_habit_${habit.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Habit",
                        tint = ErrorRed.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(category: String) {
    val (bg, fg) = when (category.uppercase()) {
        "PHYSICAL" -> Pair(MutedMilitaryGreen.copy(alpha = 0.25f), MilitaryGreenBright)
        "MIND" -> Pair(GoldAccent.copy(alpha = 0.25f), GoldBright)
        "DISCIPLINE" -> Pair(Color(0xFF3E4734), OffWhite)
        "NUTRITION" -> Pair(Color(0xFF2E4032), Color(0xFF90C28A))
        "STUDY" -> Pair(Color(0xFF38384E), Color(0xFFADC4FF))
        else -> Pair(DarkOlive, MutedGrey)
    }

    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = category.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Composable
fun QuoteCard(
    quote: QuoteEntity,
    onToggleFavorite: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SankalpCard(
        borderColor = GoldAccent.copy(alpha = 0.3f),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "“${quote.quote}”",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = OffWhite,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { onToggleFavorite(!quote.isFavorite) },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("quote_fav_button_${quote.id}")
                ) {
                    Icon(
                        imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Quote",
                        tint = if (quote.isFavorite) GoldAccent else MutedGrey,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "— ${quote.author.uppercase()}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = GoldAccent,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
