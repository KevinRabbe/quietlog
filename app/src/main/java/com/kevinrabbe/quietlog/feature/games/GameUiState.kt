package com.kevinrabbe.quietlog.feature.games

import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventType

data class GameUiState(
    val events: List<GameEvent> = emptyList(),
    val isLoading: Boolean = false,
    val isAddingEvent: Boolean = false,
    val newEventTitle: String = "",
    val newEventType: GameEventType = GameEventType.OTHER,
    val newEventDateTime: Long = System.currentTimeMillis()
)

sealed interface GameUiEvent {
    data object ToggleAddDialog : GameUiEvent
    data class TitleChanged(val title: String) : GameUiEvent
    data class TypeChanged(val type: GameEventType) : GameUiEvent
    data class DateTimeChanged(val millis: Long) : GameUiEvent
    data object SaveEvent : GameUiEvent
    data class DeleteEvent(val id: Long) : GameUiEvent
}
