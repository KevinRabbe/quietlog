package com.kevinrabbe.quietlog.feature.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.core.navigation.NavRoutes
import com.kevinrabbe.quietlog.core.util.AppFactory
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.Reminder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToTab: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory.AppFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome and Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Hello, peaceful day.",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Here is what matters next.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // 1. Optional Quiet Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTab(NavRoutes.SETTINGS) },
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isNotificationsEnabled) {
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (uiState.isNotificationsEnabled) {
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        }
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isNotificationsEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                        contentDescription = "Status Icon",
                        tint = if (uiState.isNotificationsEnabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (uiState.isNotificationsEnabled) "Quiet Mode Active" else "Quiet Mode Suspended",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (uiState.isNotificationsEnabled) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (uiState.isNotificationsEnabled) {
                                "System Serene. Notifications are enabled and scheduled quietly."
                            } else {
                                "All reminders are muted. Tap to review settings."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 2. Empty State (if everything is empty)
            if (uiState.isDashboardEmpty) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = "Empty State",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Text(
                        text = "Nothing urgent right now.",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Add a reminder, shopping list, or app event when needed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                // 3. Today & Overdue Section
                if (uiState.todayReminders.isNotEmpty() || uiState.todayGameEvents.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        uiState.todayReminders.forEach { reminder ->
                            DashboardReminderItem(
                                reminder = reminder,
                                onClick = { onNavigateToTab(NavRoutes.REMINDERS) }
                            )
                        }
                        uiState.todayGameEvents.forEach { event ->
                            DashboardGameEventItem(
                                event = event,
                                onClick = { onNavigateToTab(NavRoutes.GAMES) }
                            )
                        }
                    }
                }

                // 4. Next Up Section (Limit to 4 sorted upcoming items)
                val upcomingItems = remember(uiState.upcomingReminders, uiState.upcomingGameEvents) {
                    val items = mutableListOf<Pair<Long, @Composable () -> Unit>>()

                    uiState.upcomingReminders.forEach { reminder ->
                        items.add(reminder.dateTimeMillis to {
                            DashboardReminderItem(
                                reminder = reminder,
                                onClick = { onNavigateToTab(NavRoutes.REMINDERS) }
                            )
                        })
                    }

                    uiState.upcomingGameEvents.forEach { event ->
                        items.add(event.timestamp to {
                            DashboardGameEventItem(
                                event = event,
                                onClick = { onNavigateToTab(NavRoutes.GAMES) }
                            )
                        })
                    }

                    items.sortBy { it.first }
                    items.map { it.second }.take(4)
                }

                if (upcomingItems.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Next Up",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        upcomingItems.forEach { itemComposable ->
                            itemComposable()
                        }
                    }
                }

                // 5. Active Shopping List Section
                if (uiState.shoppingTotalCount > 0) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Active Shopping",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )

                        val progress = uiState.shoppingCheckedCount.toFloat() / uiState.shoppingTotalCount

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTab(NavRoutes.SHOPPING) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Shopping Cart",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Shopping List Progress",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${uiState.shoppingCheckedCount} of ${uiState.shoppingTotalCount} items completed",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 6. Next App Event Section
                uiState.nextAppEvent?.let { appEvent ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Next App Event",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )

                        val context = LocalContext.current
                        val pm = context.packageManager
                        val appLabel = remember(appEvent.packageName) {
                            appEvent.packageName?.let { pkg ->
                                try {
                                    pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString()
                                } catch (e: Exception) {
                                    pkg.substringAfterLast('.')
                                }
                            } ?: "Linked App"
                        }

                        val launchIntent = remember(appEvent.packageName) {
                            appEvent.packageName?.let { pm.getLaunchIntentForPackage(it) }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTab(NavRoutes.GAMES) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                            ),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SportsEsports,
                                        contentDescription = "App Event",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = appEvent.title,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "App: $appLabel",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val formattedTime = remember(appEvent.timestamp) {
                                        formatEventTime(appEvent.timestamp)
                                    }
                                    Text(
                                        text = formattedTime,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    if (launchIntent != null) {
                                        Button(
                                            onClick = {
                                                try {
                                                    context.startActivity(launchIntent)
                                                } catch (e: Exception) {
                                                    // Silent fallback
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = "Launch",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
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

        // 7. Quick Actions Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Add Reminder",
                    icon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(NavRoutes.REMINDERS) }
                )
                QuickActionCard(
                    title = "Add Shopping",
                    icon = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(NavRoutes.SHOPPING) }
                )
                QuickActionCard(
                    title = "Add Event",
                    icon = Icons.Default.SportsEsports,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab(NavRoutes.GAMES) }
                )
            }
        }
    }
}

@Composable
fun DashboardReminderItem(reminder: Reminder, onClick: () -> Unit) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeString = timeFormat.format(Date(reminder.dateTimeMillis))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Reminder",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (reminder.notes.isNotBlank()) {
                    Text(
                        text = reminder.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = timeString,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DashboardGameEventItem(event: GameEvent, onClick: () -> Unit) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeString = timeFormat.format(Date(event.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = "Game Event",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.eventType.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = timeString,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatEventTime(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val eventCal = Calendar.getInstance().apply { timeInMillis = timestamp }

    val isToday = now.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == eventCal.get(Calendar.DAY_OF_YEAR)

    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val isTomorrow = tomorrow.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
            tomorrow.get(Calendar.DAY_OF_YEAR) == eventCal.get(Calendar.DAY_OF_YEAR)

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(date)

    return when {
        isToday -> "Today at $timeString"
        isTomorrow -> "Tomorrow at $timeString"
        else -> {
            val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            dateFormat.format(date)
        }
    }
}
