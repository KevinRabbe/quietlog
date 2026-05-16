package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.repository.ReminderRepository

class DeleteReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteReminder(id)
}
