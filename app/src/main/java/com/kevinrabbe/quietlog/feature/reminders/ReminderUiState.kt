package com.kevinrabbe.quietlog.feature.reminders

import com.kevinrabbe.quietlog.domain.model.Reminder

data class ReminderUiState(
    val reminders: List<Reminder> = emptyList(),
    val isLoading: Boolean = false,
    val isAddingReminder: Boolean = false,
    val newReminderTitle: String = "",
    val newReminderDateTime: Long = System.currentTimeMillis()
)
