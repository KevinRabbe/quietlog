package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository

class CreateReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder): Long = repository.createReminder(reminder)
}
