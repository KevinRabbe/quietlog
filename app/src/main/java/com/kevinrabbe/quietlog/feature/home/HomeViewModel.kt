package com.kevinrabbe.quietlog.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.model.ReminderStatus
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import com.kevinrabbe.quietlog.domain.repository.SettingsRepository
import com.kevinrabbe.quietlog.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

/**
 * ViewModel for the calm daily dashboard.
 */
class HomeViewModel(
    private val reminderRepository: ReminderRepository,
    private val shoppingRepository: ShoppingRepository,
    private val gameEventRepository: GameEventRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        reminderRepository.observeReminders(),
        shoppingRepository.observeItems(),
        gameEventRepository.observeEvents(),
        settingsRepository.isNotificationsEnabled
    ) { reminders, shoppingItems, gameEvents, isNotificationsEnabled ->
        val now = System.currentTimeMillis()

        // Get calendar times for today
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfToday = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfToday = calendar.timeInMillis

        // Reminders due today or overdue
        val todayReminders = reminders.filter { reminder ->
            reminder.status == ReminderStatus.ACTIVE && reminder.dateTimeMillis < endOfToday
        }.sortedBy { it.dateTimeMillis }

        // Game events today
        val todayGameEvents = gameEvents.filter { event ->
            event.timestamp in startOfToday..endOfToday
        }.sortedBy { it.timestamp }

        // Next up upcoming reminders (after today)
        val upcomingReminders = reminders.filter { reminder ->
            reminder.status == ReminderStatus.ACTIVE && reminder.dateTimeMillis >= endOfToday
        }

        // Next up upcoming game events (after today)
        val upcomingGameEvents = gameEvents.filter { event ->
            event.timestamp >= endOfToday
        }

        // Shopping progress
        val shoppingTotalCount = shoppingItems.size
        val shoppingCheckedCount = shoppingItems.count { it.isChecked }

        // Next app-linked event (packageName is not null/empty, event is in the future)
        val nextAppEvent = gameEvents
            .filter { it.packageName != null && it.packageName.isNotBlank() && it.timestamp > now }
            .minByOrNull { it.timestamp }

        HomeUiState(
            todayReminders = todayReminders,
            todayGameEvents = todayGameEvents,
            upcomingReminders = upcomingReminders,
            upcomingGameEvents = upcomingGameEvents,
            shoppingCheckedCount = shoppingCheckedCount,
            shoppingTotalCount = shoppingTotalCount,
            shoppingItems = shoppingItems,
            nextAppEvent = nextAppEvent,
            isNotificationsEnabled = isNotificationsEnabled,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )
}
