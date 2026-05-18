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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.R
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
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.content_description_add_game_event))
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
                        contentDescription = stringResource(R.string.content_description_no_game_events),
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.games_empty),
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
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = { onEvent(GameUiEvent.ToggleAddDialog) },
        title = { Text(stringResource(R.string.add_game_event)) },
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
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                EventTypeSelector(
                    selectedType = uiState.newEventType,
                    onTypeSelected = { onEvent(GameUiEvent.TypeChanged(it)) }
                )

                AppSelector(
                    selectedPackageName = uiState.newEventPackageName,
                    installedApps = uiState.installedApps,
                    onAppSelected = { onEvent(GameUiEvent.PackageNameChanged(it)) }
                )

                TimeDateSelectors(
                    dateTimeMillis = uiState.newEventDateTime,
                    isTimeSelected = uiState.isTimeSelected,
                    repeatRule = uiState.newEventRepeatRule,
                    onDateTimeChanged = { onEvent(GameUiEvent.DateTimeChanged(it)) }
                )

                RepeatRuleSelector(
                    selectedRule = uiState.newEventRepeatRule,
                    onRuleSelected = { onEvent(GameUiEvent.RepeatRuleChanged(it)) }
                )

                ReminderOffsetSelector(
                    selectedOffset = uiState.newEventReminderOffset,
                    onOffsetSelected = { onEvent(GameUiEvent.ReminderOffsetChanged(it)) }
                )

                NotificationModeSelector(
                    selectedMode = uiState.newEventNotificationMode,
                    onModeSelected = { onEvent(GameUiEvent.NotificationModeChanged(it)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(GameUiEvent.SaveEvent) },
                enabled = uiState.newEventTitle.isNotBlank() && uiState.isTimeSelected
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(GameUiEvent.ToggleAddDialog) }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventTypeSelector(
    selectedType: GameEventType,
    onTypeSelected: (GameEventType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedType.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            GameEventType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppSelector(
    selectedPackageName: String?,
    installedApps: List<Pair<String, String>>,
    onAppSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedAppName = selectedPackageName?.let { pkg ->
        installedApps.find { it.second == pkg }?.first
    } ?: "None"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedAppName,
            onValueChange = {},
            readOnly = true,
            label = { Text("App/Game") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("None") },
                onClick = {
                    onAppSelected(null)
                    expanded = false
                }
            )
            installedApps.forEach { (label, pkg) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onAppSelected(pkg)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TimeDateSelectors(
    dateTimeMillis: Long,
    isTimeSelected: Boolean,
    repeatRule: RepeatRule,
    onDateTimeChanged: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember(dateTimeMillis) {
        Calendar.getInstance().apply { timeInMillis = dateTimeMillis }
    }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    val datePickerDialog = remember(calendar) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year, month, dayOfMonth, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
                onDateTimeChanged(c.timeInMillis)
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
                c.timeInMillis = dateTimeMillis
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                onDateTimeChanged(c.timeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { timePicker.show() }) {
            Text(if (isTimeSelected) timeFormat.format(Date(dateTimeMillis)) else "Select Time")
        }
        if (repeatRule == RepeatRule.NONE || repeatRule == RepeatRule.MONTHLY) {
            TextButton(onClick = { datePickerDialog.show() }) {
                Text(if (isTimeSelected) dateFormat.format(Date(dateTimeMillis)) else "Select Date")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepeatRuleSelector(
    selectedRule: RepeatRule,
    onRuleSelected: (RepeatRule) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedRule.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Repeat") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            RepeatRule.entries.forEach { rule ->
                DropdownMenuItem(
                    text = { Text(rule.name) },
                    onClick = {
                        onRuleSelected(rule)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderOffsetSelector(
    selectedOffset: Int,
    onOffsetSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val offsetText = if (selectedOffset == 0) "At event time" else "$selectedOffset minutes before"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = offsetText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Reminder Offset") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf(0, 5, 10, 15, 20, 30, 60).forEach { offset ->
                DropdownMenuItem(
                    text = { Text(if (offset == 0) "At event time" else "$offset minutes before") },
                    onClick = {
                        onOffsetSelected(offset)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationModeSelector(
    selectedMode: NotificationMode,
    onModeSelected: (NotificationMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedMode.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Notification Mode") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            NotificationMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.name) },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}
