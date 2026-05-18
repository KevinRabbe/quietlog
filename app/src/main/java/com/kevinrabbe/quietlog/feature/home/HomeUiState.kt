package com.kevinrabbe.quietlog.feature.home

import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem

/**
 * UI State for the calm daily dashboard.
 */
data class HomeUiState(
    val todayReminders: List<Reminder> = emptyList(),
    val todayGameEvents: List<GameEvent> = emptyList(),
    val upcomingReminders: List<Reminder> = emptyList(),
    val upcomingGameEvents: List<GameEvent> = emptyList(),
    val shoppingCheckedCount: Int = 0,
    val shoppingTotalCount: Int = 0,
    val shoppingItems: List<ShoppingListItem> = emptyList(),
    val nextAppEvent: GameEvent? = null,
    val isNotificationsEnabled: Boolean = true,
    val isLoading: Boolean = false
) {
    val isDashboardEmpty: Boolean
        get() = todayReminders.isEmpty() &&
                todayGameEvents.isEmpty() &&
                upcomingReminders.isEmpty() &&
                upcomingGameEvents.isEmpty() &&
                shoppingTotalCount == 0 &&
                nextAppEvent == null
}
