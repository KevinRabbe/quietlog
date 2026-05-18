package com.kevinrabbe.quietlog.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kevinrabbe.quietlog.domain.model.AppTheme
import com.kevinrabbe.quietlog.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")
    private val themeKey = stringPreferencesKey("app_theme")
    private val snoozeDurationKey = intPreferencesKey("snooze_duration")
    private val shoppingDelayKey = intPreferencesKey("shopping_delay")

    override val theme: Flow<AppTheme> = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[themeKey] ?: AppTheme.SYSTEM.name
            try {
                AppTheme.valueOf(themeName)
            } catch (e: Exception) {
                AppTheme.SYSTEM
            }
        }

    override suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    override val snoozeDurationMinutes: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[snoozeDurationKey] ?: 5
        }

    override suspend fun setSnoozeDurationMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[snoozeDurationKey] = minutes
        }
    }

    override val shoppingReminderDelayMinutes: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[shoppingDelayKey] ?: 5
        }

    override suspend fun setShoppingReminderDelayMinutes(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[shoppingDelayKey] = minutes
        }
    }

    override val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[notificationsEnabledKey] ?: true
        }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = enabled
        }
    }
}
