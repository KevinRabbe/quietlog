package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import com.kevinrabbe.quietlog.domain.repository.ReminderScheduler

class CreateReminderUseCase(
    private val repository: ReminderRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke(reminder: Reminder): Long {
        val id = repository.createReminder(reminder)
        scheduler.schedule(reminder.copy(id = id))
        return id
    }
}
