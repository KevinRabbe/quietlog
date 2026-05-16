package com.kevinrabbe.quietlog.feature.reminders

sealed interface ReminderEvent {
    data object ToggleAddDialog : ReminderEvent
    data class TitleChanged(val title: String) : ReminderEvent
    data class DateTimeChanged(val millis: Long) : ReminderEvent
    data object SaveReminder : ReminderEvent
    data class CompleteReminder(val id: Long) : ReminderEvent
    data class DeleteReminder(val id: Long) : ReminderEvent
    data class ChangeFilter(val filter: ReminderFilter) : ReminderEvent
}
