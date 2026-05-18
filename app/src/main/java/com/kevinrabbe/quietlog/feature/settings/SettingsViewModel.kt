package com.kevinrabbe.quietlog.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.AppTheme
import com.kevinrabbe.quietlog.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isNotificationsEnabled: Boolean = true,
    val theme: AppTheme = AppTheme.SYSTEM,
    val snoozeDurationMinutes: Int = 5,
    val shoppingReminderDelayMinutes: Int = 5
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.isNotificationsEnabled,
        settingsRepository.theme,
        settingsRepository.snoozeDurationMinutes,
        settingsRepository.shoppingReminderDelayMinutes
    ) { notifications, theme, snooze, shoppingDelay ->
        SettingsUiState(
            isNotificationsEnabled = notifications,
            theme = theme,
            snoozeDurationMinutes = snooze,
            shoppingReminderDelayMinutes = shoppingDelay
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun setSnoozeDuration(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.setSnoozeDurationMinutes(minutes)
        }
    }

    fun setShoppingDelay(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.setShoppingReminderDelayMinutes(minutes)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }
}
