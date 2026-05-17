package com.kevinrabbe.quietlog.feature.games

import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventType

import com.kevinrabbe.quietlog.domain.model.NotificationMode
import com.kevinrabbe.quietlog.domain.model.RepeatRule

data class GameUiState(
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

sealed interface GameUiEvent {
    data object ToggleAddDialog : GameUiEvent
    data class TitleChanged(val title: String) : GameUiEvent
    data class TypeChanged(val type: GameEventType) : GameUiEvent
    data class DateTimeChanged(val millis: Long) : GameUiEvent
    data class PackageNameChanged(val packageName: String?) : GameUiEvent
    data class ReminderOffsetChanged(val offsetMinutes: Int) : GameUiEvent
    data class RepeatRuleChanged(val rule: RepeatRule) : GameUiEvent
    data class NotificationModeChanged(val mode: NotificationMode) : GameUiEvent
    data object SaveEvent : GameUiEvent
    data class DeleteEvent(val id: Long) : GameUiEvent
}
