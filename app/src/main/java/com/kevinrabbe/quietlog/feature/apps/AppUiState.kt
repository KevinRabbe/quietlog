package com.kevinrabbe.quietlog.feature.apps

import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventType
import com.kevinrabbe.quietlog.domain.model.NotificationMode
import com.kevinrabbe.quietlog.domain.model.RepeatRule

data class AppUiState(
    val events: List<GameEvent> = emptyList(),
    val isLoading: Boolean = false,
    val isAddingEvent: Boolean = false,
    val newEventTitle: String = "",
    val newEventType: GameEventType = GameEventType.OTHER,
    val newEventDateTime: Long = System.currentTimeMillis(),
    val isTimeSelected: Boolean = false,
    val newEventPackageName: String? = null,
    val newEventReminderOffset: Int = 0,
    val newEventRepeatRule: RepeatRule = RepeatRule.NONE,
    val newEventNotificationMode: NotificationMode = NotificationMode.NOTIFICATION,
    val installedApps: List<Pair<String, String>> = emptyList()
)

sealed interface AppUiEvent {
    data object ToggleAddDialog : AppUiEvent
    data class TitleChanged(val title: String) : AppUiEvent
    data class TypeChanged(val type: GameEventType) : AppUiEvent
    data class DateTimeChanged(val millis: Long) : AppUiEvent
    data class PackageNameChanged(val packageName: String?) : AppUiEvent
    data class ReminderOffsetChanged(val offsetMinutes: Int) : AppUiEvent
    data class RepeatRuleChanged(val rule: RepeatRule) : AppUiEvent
    data class NotificationModeChanged(val mode: NotificationMode) : AppUiEvent
    data object SaveEvent : AppUiEvent
    data class DeleteEvent(val id: Long) : AppUiEvent
}
