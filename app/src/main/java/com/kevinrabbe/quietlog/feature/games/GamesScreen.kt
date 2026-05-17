package com.kevinrabbe.quietlog.feature.games

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.core.util.AppFactory
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventType
import com.kevinrabbe.quietlog.domain.model.NotificationMode
import com.kevinrabbe.quietlog.domain.model.RepeatRule
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GamesScreen(
    viewModel: GameViewModel = viewModel(factory = ViewModelProvider.Factory.AppFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(GameUiEvent.ToggleAddDialog) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.events.isEmpty() && !uiState.isLoading) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "No game events",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No game events yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.events, key = { it.id }) { event ->
                        GameEventItem(
                            event = event,
                            onDelete = { viewModel.onEvent(GameUiEvent.DeleteEvent(event.id)) }
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (uiState.isAddingEvent) {
                AddGameEventDialog(
                    uiState = uiState,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}

@Composable
fun GameEventItem(
    event: GameEvent,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }
    val dateString = dateFormat.format(Date(event.timestamp))
    val context = LocalContext.current
    val pm = context.packageManager

    val appName = remember(event.packageName) {
        event.packageName?.let {
            try {
                val appInfo = pm.getApplicationInfo(it, 0)
                pm.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                null
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (event.eventType) {
                            GameEventType.RAID -> Icons.Default.Groups
                            GameEventType.SPAWN -> Icons.Default.Timer
                            GameEventType.MATCH -> Icons.Default.SportsEsports
                            GameEventType.TOURNAMENT -> Icons.Default.EmojiEvents
                            GameEventType.OTHER -> Icons.Default.Event
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                val metaText = buildList {
                    if (appName != null) add("App: $appName")
                    add("Time: $dateString")
                    add("Repeat: ${event.repeatRule.name.lowercase().replaceFirstChar { it.uppercase() }}")
                    add("Reminder: ${if (event.reminderOffset == 0) "At event time" else "${event.reminderOffset} min before"}")
                    add("Notification: ${event.notificationMode.name.lowercase().replaceFirstChar { it.uppercase() }}")
                }.joinToString("\n")

                Text(
                    text = metaText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameEventDialog(
    uiState: GameUiState,
    onEvent: (GameUiEvent) -> Unit
) {
    val context = LocalContext.current

    var appExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var repeatExpanded by remember { mutableStateOf(false) }
    var offsetExpanded by remember { mutableStateOf(false) }
    var modeExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val calendar = remember(uiState.newEventDateTime) {
        Calendar.getInstance().apply { timeInMillis = uiState.newEventDateTime }
    }

    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    val timePickerDialog = remember(calendar) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year, month, dayOfMonth, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
                onEvent(GameUiEvent.DateTimeChanged(c.timeInMillis))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val timePicker = remember(calendar) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val c = Calendar.getInstance()
                c.timeInMillis = uiState.newEventDateTime
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                onEvent(GameUiEvent.DateTimeChanged(c.timeInMillis))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    AlertDialog(
        onDismissRequest = { onEvent(GameUiEvent.ToggleAddDialog) },
        title = { Text("Add Game Event") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = uiState.newEventTitle,
                    onValueChange = { onEvent(GameUiEvent.TitleChanged(it)) },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Event Type Selector
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    TextField(
                        value = uiState.newEventType.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        GameEventType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    onEvent(GameUiEvent.TypeChanged(type))
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                // App Selector
                ExposedDropdownMenuBox(
                    expanded = appExpanded,
                    onExpandedChange = { appExpanded = !appExpanded }
                ) {
                    val selectedAppName = uiState.newEventPackageName?.let { pkg ->
                        uiState.installedApps.find { it.second == pkg }?.first
                    } ?: "None"

                    TextField(
                        value = selectedAppName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("App/Game") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = appExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = appExpanded,
                        onDismissRequest = { appExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                onEvent(GameUiEvent.PackageNameChanged(null))
                                appExpanded = false
                            }
                        )
                        uiState.installedApps.forEach { (label, pkg) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onEvent(GameUiEvent.PackageNameChanged(pkg))
                                    appExpanded = false
                                }
                            )
                        }
                    }
                }

                // Time Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { timePicker.show() }) {
                        Text(if (uiState.isTimeSelected) timeFormat.format(Date(uiState.newEventDateTime)) else "Select Time")
                    }
                    if (uiState.newEventRepeatRule == RepeatRule.NONE || uiState.newEventRepeatRule == RepeatRule.MONTHLY) {
                        TextButton(onClick = { timePickerDialog.show() }) {
                            Text(if (uiState.isTimeSelected) dateFormat.format(Date(uiState.newEventDateTime)) else "Select Date")
                        }
                    }
                }

                // Repeat Rule
                ExposedDropdownMenuBox(
                    expanded = repeatExpanded,
                    onExpandedChange = { repeatExpanded = !repeatExpanded }
                ) {
                    TextField(
                        value = uiState.newEventRepeatRule.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Repeat") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = repeatExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = repeatExpanded,
                        onDismissRequest = { repeatExpanded = false }
                    ) {
                        RepeatRule.entries.forEach { rule ->
                            DropdownMenuItem(
                                text = { Text(rule.name) },
                                onClick = {
                                    onEvent(GameUiEvent.RepeatRuleChanged(rule))
                                    repeatExpanded = false
                                }
                            )
                        }
                    }
                }

                // Reminder Offset
                ExposedDropdownMenuBox(
                    expanded = offsetExpanded,
                    onExpandedChange = { offsetExpanded = !offsetExpanded }
                ) {
                    val offsetText = if (uiState.newEventReminderOffset == 0) "At event time" else "${uiState.newEventReminderOffset} minutes before"
                    TextField(
                        value = offsetText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Reminder Offset") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = offsetExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = offsetExpanded,
                        onDismissRequest = { offsetExpanded = false }
                    ) {
                        listOf(0, 5, 10, 15, 20, 30, 60).forEach { offset ->
                            DropdownMenuItem(
                                text = { Text(if (offset == 0) "At event time" else "$offset minutes before") },
                                onClick = {
                                    onEvent(GameUiEvent.ReminderOffsetChanged(offset))
                                    offsetExpanded = false
                                }
                            )
                        }
                    }
                }

                // Notification Mode
                ExposedDropdownMenuBox(
                    expanded = modeExpanded,
                    onExpandedChange = { modeExpanded = !modeExpanded }
                ) {
                    TextField(
                        value = uiState.newEventNotificationMode.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Notification Mode") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modeExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = modeExpanded,
                        onDismissRequest = { modeExpanded = false }
                    ) {
                        NotificationMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.name) },
                                onClick = {
                                    onEvent(GameUiEvent.NotificationModeChanged(mode))
                                    modeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(GameUiEvent.SaveEvent) },
                enabled = uiState.newEventTitle.isNotBlank() && uiState.isTimeSelected
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(GameUiEvent.ToggleAddDialog) }) {
                Text("Cancel")
            }
        }
    )
}
