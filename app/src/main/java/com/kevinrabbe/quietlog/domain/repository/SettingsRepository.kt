package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)

    val snoozeDurationMinutes: Flow<Int>
    suspend fun setSnoozeDurationMinutes(minutes: Int)

    val shoppingReminderDelayMinutes: Flow<Int>
    suspend fun setShoppingReminderDelayMinutes(minutes: Int)

    val isNotificationsEnabled: Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
