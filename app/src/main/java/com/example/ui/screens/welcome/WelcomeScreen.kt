package com.example.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SankalpButton
import com.example.ui.components.SankalpCard
import com.example.ui.theme.BorderOlive
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.DarkOlive
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.MutedGrey
import com.example.ui.theme.MutedMilitaryGreen
import com.example.ui.theme.OffWhite

@Composable
fun WelcomeScreen(
    onBeginMission: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var commanderName by remember { mutableStateOf("Commander") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo Badge
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, GoldAccent, CircleShape)
                    .background(DarkOlive),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sankalp_crest),
                    contentDescription = "Sankalp Emblem",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "SANKALP",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = OffWhite,
                    letterSpacing = 4.sp,
                    fontSize = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tagline
            Text(
                text = "DISCIPLINE BUILDS DESTINY",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    letterSpacing = 2.5.sp,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Motivational Doctrine Card
            SankalpCard(
                borderColor = GoldAccent.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "“We are what we repeatedly do. Excellence, then, is not an act, but a habit.”",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = OffWhite,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "— ARISTOTLE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MutedMilitaryGreen,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Name Personalization
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "ENTER CALLSIGN / NAME",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MutedGrey,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = commanderName,
                    onValueChange = { commanderName = it },
                    placeholder = { Text("e.g. Commander or Alex", color = MutedGrey) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkOlive,
                        unfocusedContainerColor = DarkOlive,
                        focusedTextColor = OffWhite,
                        unfocusedTextColor = OffWhite,
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = BorderOlive,
                        cursorColor = GoldAccent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("name_input_field")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Begin Button
            SankalpButton(
                text = "BEGIN YOUR MISSION",
                onClick = {
                    val finalName = commanderName.trim().ifEmpty { "Commander" }
                    onBeginMission(finalName)
                },
                modifier = Modifier.fillMaxWidth(),
                testTag = "begin_mission_button"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tactical Habit Tracking • Focus Timer • Offline Storage",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MutedGrey,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
