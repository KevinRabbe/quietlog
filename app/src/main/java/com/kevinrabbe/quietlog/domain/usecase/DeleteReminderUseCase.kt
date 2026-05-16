package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.repository.ReminderRepository

class DeleteReminderUseCase(
    private val repository: ReminderRepository,
    private val scheduler: ReminderScheduler
) {
    suspend operator fun invoke(id: Long) {
        scheduler.cancel(id)
        repository.deleteReminder(id)
    }
}
