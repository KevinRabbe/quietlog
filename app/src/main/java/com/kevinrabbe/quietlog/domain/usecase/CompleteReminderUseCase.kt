package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.ReminderStatus
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository

class CompleteReminderUseCase(
    private val repository: ReminderRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke(id: Long) {
        val reminder = repository.getReminderById(id)
        if (reminder != null) {
            scheduler.cancel(id)
            repository.updateReminder(
                reminder.copy(
                    status = ReminderStatus.COMPLETED,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
