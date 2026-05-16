package com.kevinrabbe.quietlog.feature.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameEventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        observeEvents()
    }

    private fun observeEvents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.observeEvents().collect { events ->
                _uiState.update { it.copy(events = events, isLoading = false) }
            }
        }
    }

    fun onEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.ToggleAddDialog -> {
                _uiState.update { it.copy(isAddingEvent = !it.isAddingEvent) }
            }
            is GameUiEvent.TitleChanged -> {
                _uiState.update { it.copy(newEventTitle = event.title) }
            }
            is GameUiEvent.TypeChanged -> {
                _uiState.update { it.copy(newEventType = event.type) }
            }
            is GameUiEvent.DateTimeChanged -> {
                _uiState.update { it.copy(newEventDateTime = event.millis) }
            }
            is GameUiEvent.SaveEvent -> {
                saveEvent()
            }
            is GameUiEvent.DeleteEvent -> {
                viewModelScope.launch {
                    repository.deleteEvent(event.id)
                }
            }
        }
    }

    private fun saveEvent() {
        val title = _uiState.value.newEventTitle
        if (title.isBlank()) return

        viewModelScope.launch {
            repository.insertEvent(
                GameEvent(
                    title = title,
                    eventType = _uiState.value.newEventType,
                    timestamp = _uiState.value.newEventDateTime
                )
            )
            _uiState.update {
                it.copy(
                    isAddingEvent = false,
                    newEventTitle = ""
                )
            }
        }
    }
}
