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

import com.kevinrabbe.quietlog.core.notification.GameEventScheduler
import java.util.Calendar

import android.app.Application
import android.content.Intent
import kotlinx.coroutines.Dispatchers

class GameViewModel(
    private val application: Application,
    private val repository: GameEventRepository,
    private val scheduler: GameEventScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

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
            repository.observeEvents().collect { events ->
                _uiState.update { it.copy(events = events, isLoading = false) }
            }
        }
    }

    fun onEvent(event: GameUiEvent) {
        when (event) {
            is GameUiEvent.ToggleAddDialog -> {
                android.util.Log.d("QuietLog", "Game ToggleAddDialog clicked! Current isAddingEvent = ${_uiState.value.isAddingEvent}")
                _uiState.update { it.copy(isAddingEvent = !it.isAddingEvent) }
                android.util.Log.d("QuietLog", "Game ToggleAddDialog finished! New isAddingEvent = ${_uiState.value.isAddingEvent}")
            }
            is GameUiEvent.TitleChanged -> {
                _uiState.update { it.copy(newEventTitle = event.title) }
            }
            is GameUiEvent.TypeChanged -> {
                _uiState.update { it.copy(newEventType = event.type) }
            }
            is GameUiEvent.DateTimeChanged -> {
                _uiState.update { it.copy(newEventDateTime = event.millis, isTimeSelected = true) }
            }
            is GameUiEvent.PackageNameChanged -> {
                _uiState.update { it.copy(newEventPackageName = event.packageName) }
            }
            is GameUiEvent.ReminderOffsetChanged -> {
                _uiState.update { it.copy(newEventReminderOffset = event.offsetMinutes) }
            }
            is GameUiEvent.RepeatRuleChanged -> {
                _uiState.update { it.copy(newEventRepeatRule = event.rule) }
            }
            is GameUiEvent.NotificationModeChanged -> {
                _uiState.update { it.copy(newEventNotificationMode = event.mode) }
            }
            is GameUiEvent.SaveEvent -> {
                saveEvent()
            }
            is GameUiEvent.DeleteEvent -> {
                viewModelScope.launch {
                    repository.deleteEvent(event.id)
                    scheduler.cancel(event.id)
                }
            }
        }
    }

    private fun saveEvent() {
        val state = _uiState.value
        val title = state.newEventTitle
        if (title.isBlank() || !state.isTimeSelected) return

        // If repeat rule is a weekday, we adjust the timestamp to the next occurrence of that weekday with the selected time
        var eventTime = state.newEventDateTime
        val repeatRule = state.newEventRepeatRule
        if (repeatRule != com.kevinrabbe.quietlog.domain.model.RepeatRule.NONE && repeatRule != com.kevinrabbe.quietlog.domain.model.RepeatRule.DAILY) {
            val targetDayOfWeek = when (repeatRule) {
                com.kevinrabbe.quietlog.domain.model.RepeatRule.SUNDAY -> Calendar.SUNDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.MONDAY -> Calendar.MONDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.TUESDAY -> Calendar.TUESDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.WEDNESDAY -> Calendar.WEDNESDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.THURSDAY -> Calendar.THURSDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.FRIDAY -> Calendar.FRIDAY
                com.kevinrabbe.quietlog.domain.model.RepeatRule.SATURDAY -> Calendar.SATURDAY
                else -> -1
            }
            if (targetDayOfWeek != -1) {
                val cal = Calendar.getInstance().apply { timeInMillis = eventTime }
                val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                var daysToAdd = targetDayOfWeek - currentDayOfWeek
                if (daysToAdd < 0 || (daysToAdd == 0 && eventTime < System.currentTimeMillis())) {
                    daysToAdd += 7
                }
                cal.add(Calendar.DAY_OF_YEAR, daysToAdd)
                eventTime = cal.timeInMillis
            }
        } else if (repeatRule == com.kevinrabbe.quietlog.domain.model.RepeatRule.DAILY) {
            val cal = Calendar.getInstance().apply { timeInMillis = eventTime }
            val alarmTime = eventTime - (state.newEventReminderOffset * 60_000L)
            if (alarmTime < System.currentTimeMillis()) {
                cal.add(Calendar.DAY_OF_YEAR, 1)
                eventTime = cal.timeInMillis
            }
        }

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
            val id = repository.insertEventAndGetId(event)
            scheduler.schedule(event.copy(id = id))

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
