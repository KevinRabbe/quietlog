package com.kevinrabbe.quietlog.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isNotificationsEnabled: Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
