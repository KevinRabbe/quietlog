package com.kevinrabbe.quietlog.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.R
import com.kevinrabbe.quietlog.core.util.AppFactory
import com.kevinrabbe.quietlog.domain.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = ViewModelProvider.Factory.AppFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings_title)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Theme Section
            SettingsSection(title = stringResource(R.string.settings_theme_title)) {
                ThemeSelector(
                    selectedTheme = uiState.theme,
                    onThemeSelected = { viewModel.setTheme(it) }
                )
            }

            // Notifications Section
            SettingsSection(title = stringResource(R.string.settings_notifications_title)) {
                NotificationToggle(
                    isEnabled = uiState.isNotificationsEnabled,
                    onToggle = { viewModel.toggleNotifications(it) }
                )
            }

            // Snooze Section
            SettingsSection(title = stringResource(R.string.settings_snooze_title)) {
                DurationSelector(
                    selectedMinutes = uiState.snoozeDurationMinutes,
                    onMinutesSelected = { viewModel.setSnoozeDuration(it) }
                )
            }

            // Shopping Delay Section
            SettingsSection(title = stringResource(R.string.settings_shopping_delay_title)) {
                DurationSelector(
                    selectedMinutes = uiState.shoppingReminderDelayMinutes,
                    onMinutesSelected = { viewModel.setShoppingDelay(it) }
                )
            }

            // About Section
            SettingsSection(title = stringResource(R.string.settings_about_title)) {
                AboutCard()
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSelector(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        AppTheme.entries.forEachIndexed { index, theme ->
            SegmentedButton(
                selected = selectedTheme == theme,
                onClick = { onThemeSelected(theme) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = AppTheme.entries.size)
            ) {
                Text(
                    when (theme) {
                        AppTheme.SYSTEM -> stringResource(R.string.settings_theme_system)
                        AppTheme.LIGHT -> stringResource(R.string.settings_theme_light)
                        AppTheme.DARK -> stringResource(R.string.settings_theme_dark)
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationToggle(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.settings_enable_notifications),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.settings_enable_notifications_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationSelector(
    selectedMinutes: Int,
    onMinutesSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(5, 10, 15, 30, 60)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = stringResource(R.string.settings_minutes, selectedMinutes),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { minutes ->
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.settings_minutes, minutes)) },
                    onClick = {
                        onMinutesSelected(minutes)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AboutCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_version, "0.1.0"),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.settings_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
