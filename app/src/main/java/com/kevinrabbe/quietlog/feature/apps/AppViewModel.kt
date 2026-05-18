package com.kevinrabbe.quietlog.feature.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.usecase.CreateGameEventUseCase
import com.kevinrabbe.quietlog.domain.usecase.DeleteGameEventUseCase
import com.kevinrabbe.quietlog.domain.usecase.ObserveGameEventsUseCase
import com.kevinrabbe.quietlog.domain.util.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import android.app.Application
import android.content.Intent
import kotlinx.coroutines.Dispatchers

class AppViewModel(
    private val application: Application,
    private val observeGameEventsUseCase: ObserveGameEventsUseCase,
    private val createGameEventUseCase: CreateGameEventUseCase,
    private val deleteGameEventUseCase: DeleteGameEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        observeEvents()
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pm = application.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }
                val apps = pm.queryIntentActivities(intent, 0)
                    .map { it.activityInfo.applicationInfo }
                    .distinctBy { it.packageName }
                    .map { pm.getApplicationLabel(it).toString() to it.packageName }
                    .sortedBy { it.first }

                _uiState.update { it.copy(installedApps = apps) }
            } catch (e: Exception) {
                android.util.Log.e("QuietLog", "Failed to load installed apps", e)
            }
        }
    }

    private fun observeEvents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            observeGameEventsUseCase().collect { events ->
                _uiState.update { it.copy(events = events, isLoading = false) }
            }
        }
    }

    fun onEvent(event: AppUiEvent) {
        when (event) {
            is AppUiEvent.ToggleAddDialog -> {
                android.util.Log.d("QuietLog", "App ToggleAddDialog clicked! Current isAddingEvent = ${_uiState.value.isAddingEvent}")
                _uiState.update { it.copy(isAddingEvent = !it.isAddingEvent) }
                android.util.Log.d("QuietLog", "App ToggleAddDialog finished! New isAddingEvent = ${_uiState.value.isAddingEvent}")
            }
            is AppUiEvent.TitleChanged -> {
                _uiState.update { it.copy(newEventTitle = event.title) }
            }
            is AppUiEvent.TypeChanged -> {
                _uiState.update { it.copy(newEventType = event.type) }
            }
            is AppUiEvent.DateTimeChanged -> {
                _uiState.update { it.copy(newEventDateTime = event.millis, isTimeSelected = true) }
            }
            is AppUiEvent.PackageNameChanged -> {
                _uiState.update { it.copy(newEventPackageName = event.packageName) }
            }
            is AppUiEvent.ReminderOffsetChanged -> {
                _uiState.update { it.copy(newEventReminderOffset = event.offsetMinutes) }
            }
            is AppUiEvent.RepeatRuleChanged -> {
                _uiState.update { it.copy(newEventRepeatRule = event.rule) }
            }
            is AppUiEvent.NotificationModeChanged -> {
                _uiState.update { it.copy(newEventNotificationMode = event.mode) }
            }
            is AppUiEvent.SaveEvent -> {
                saveEvent()
            }
            is AppUiEvent.DeleteEvent -> {
                viewModelScope.launch {
                    deleteGameEventUseCase(event.id)
                }
            }
        }
    }

    private fun saveEvent() {
        val state = _uiState.value
        val title = state.newEventTitle
        if (title.isBlank() || !state.isTimeSelected) return

        // Calculate event time considering reminder offset to ensure the notification is in the future
        val eventTime = TimeUtils.calculateNextOccurrence(
            baseTimeMillis = state.newEventDateTime,
            rule = state.newEventRepeatRule,
            now = System.currentTimeMillis() + (state.newEventReminderOffset * 60_000L)
        )

        viewModelScope.launch {
            val event = GameEvent(
                title = title,
                eventType = state.newEventType,
                timestamp = eventTime,
                packageName = state.newEventPackageName,
                reminderOffset = state.newEventReminderOffset,
                repeatRule = state.newEventRepeatRule,
                notificationMode = state.newEventNotificationMode
            )
            createGameEventUseCase(event)

            _uiState.update {
                it.copy(
                    isAddingEvent = false,
                    newEventTitle = "",
                    isTimeSelected = false,
                    newEventPackageName = null,
                    newEventReminderOffset = 0,
                    newEventRepeatRule = com.kevinrabbe.quietlog.domain.model.RepeatRule.NONE,
                    newEventNotificationMode = com.kevinrabbe.quietlog.domain.model.NotificationMode.NOTIFICATION
                )
            }
        }
    }
}
