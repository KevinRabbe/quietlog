package com.kevinrabbe.quietlog.feature.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.usecase.CompleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.CreateReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.DeleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.ObserveRemindersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val observeRemindersUseCase: ObserveRemindersUseCase,
    private val createReminderUseCase: CreateReminderUseCase,
    private val completeReminderUseCase: CompleteReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
        observeReminders()
    }

    private fun observeReminders() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            observeRemindersUseCase().collect { reminders ->
                _uiState.update {
                    it.copy(
                        reminders = reminders,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.ToggleAddDialog -> {
                _uiState.update { it.copy(isAddingReminder = !it.isAddingReminder) }
            }
            is ReminderEvent.TitleChanged -> {
                _uiState.update { it.copy(newReminderTitle = event.title) }
            }
            is ReminderEvent.DateTimeChanged -> {
                _uiState.update { it.copy(newReminderDateTime = event.millis) }
            }
            is ReminderEvent.SaveReminder -> {
                saveReminder()
            }
            is ReminderEvent.CompleteReminder -> {
                viewModelScope.launch {
                    completeReminderUseCase(event.id)
                }
            }
            is ReminderEvent.DeleteReminder -> {
                viewModelScope.launch {
                    deleteReminderUseCase(event.id)
                }
            }
        }
    }

    private fun saveReminder() {
        val title = _uiState.value.newReminderTitle
        if (title.isBlank()) return

        viewModelScope.launch {
            createReminderUseCase(
                Reminder(
                    title = title,
                    dateTimeMillis = _uiState.value.newReminderDateTime
                )
            )
            _uiState.update {
                it.copy(
                    isAddingReminder = false,
                    newReminderTitle = ""
                )
            }
        }
    }
}
