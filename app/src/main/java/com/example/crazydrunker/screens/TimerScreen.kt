package com.example.crazydrunker.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.crazydrunker.CocktailsData
import com.example.crazydrunker.TimerScreenData
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun Timer(
    timerData: TimerScreenData,
    navController: NavController
) {
    var minutes by rememberSaveable { mutableIntStateOf(timerData.min) }
    var seconds by rememberSaveable { mutableIntStateOf(timerData.sec) }
    var isRunning by rememberSaveable { mutableStateOf(timerData.isStarted) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val activeStepIndex = rememberSaveable { mutableIntStateOf(timerData.stepIndex) }

    var isAutoNextStep by rememberSaveable { mutableStateOf(false) }

    // Timer logic
    LaunchedEffect(minutes, seconds, isRunning) {
        while (isRunning) {
            delay(1000)
            // Update minutes and seconds
            seconds = max(seconds - 1, 0)

            // If timer is up, go to next step
            if (seconds == 0 && minutes == 0)
            {
                // Go to next step
                if (activeStepIndex.intValue < CocktailsData.getCocktailSteps(timerData.cocktailId).size - 1 && isAutoNextStep)
                {
                    activeStepIndex.intValue++
                    val currentStep = CocktailsData.getCocktailSteps(timerData.cocktailId)[activeStepIndex.intValue]

                    minutes = currentStep.min
                    seconds = currentStep.sec
                }
                else {
                    isRunning = false
                    seconds = timerData.sec
                    minutes = timerData.min
                    activeStepIndex.intValue = 0
                    break
                }
            }

            if (seconds == 0) {
                minutes = max(minutes - 1, 0)
                seconds = 59
            }

        }
    }


    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display time
        Box(
            modifier = Modifier
            .clickable(
                onClick = { if (!isRunning) showTimePicker = true }
            )
        )
        {
            Text(
                text = formatTime(minutes, seconds),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start/Pause button
            IconButton(
                onClick = {
                    isRunning = !isRunning
                },
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Lock else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start"
                )
                Spacer(Modifier.width(8.dp))
            }

            // Edit time button
            if (!isRunning) {
                IconButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Filled.Edit, "Set Time")
                }
            }

            IconButton(
                onClick = { isRunning = false; minutes = timerData.min; seconds = timerData.sec; activeStepIndex.intValue = timerData.stepIndex},
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.Refresh, "Refresh")
            }
        }

        SectionHeader(title = "Preparation")
        Row (
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text("Auto next step", Modifier.padding(12.dp))
            Switch(
                checked = isAutoNextStep,
                onCheckedChange = { isAutoNextStep = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))
        StepsList(
            cocktail = CocktailsData.getCocktail(timerData.cocktailId)!!,
            navController = navController,
            showTimers = false,
            onStepTimerClick = { min, sec -> minutes = min as Int; seconds = sec as Int; isRunning = true; },
            activeStepIndex = activeStepIndex
            )
    }

    // TimePicker dialog
    if (showTimePicker) {
        var tempMinutes by remember { mutableIntStateOf(minutes) }
        var tempSeconds by remember { mutableIntStateOf(seconds) }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Set Timer Duration") },
            text = {
                Column {
                    Text("Minutes", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = tempMinutes.toFloat(),
                        onValueChange = { tempMinutes = it.toInt() },
                        valueRange = 0f..59f,
                        steps = 58,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text("$tempMinutes min", style = MaterialTheme.typography.bodyLarge)

                    Spacer(Modifier.height(16.dp))

                    Text("Seconds", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = tempSeconds.toFloat(),
                        onValueChange = { tempSeconds = it.toInt() },
                        valueRange = 0f..59f,
                        steps = 58,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text("$tempSeconds sec", style = MaterialTheme.typography.bodyLarge)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        minutes = tempMinutes
                        seconds = tempSeconds
                        showTimePicker = false
                    }
                ) {
                    Text("Set")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


}

// Correct time formatting function
@SuppressLint("DefaultLocale")
internal fun formatTime(min: Int, sec: Int): String {
    return String.format("%02d:%02d", min, sec)
}


@Composable
fun FAP(
    onTimerClick: () -> Unit = {},
    onButton2Click: () -> Unit = {},
    onButton3Click: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                RoundButton(icon = Icons.Default.Notifications, onClick = onTimerClick)
                RoundButton(icon = Icons.Default.Add, onClick = onButton2Click)
                RoundButton(icon = Icons.Default.Close, onClick = onButton3Click)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Expand",
                tint = Color.White
            )
        }
    }
}


@Composable
fun RoundButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}