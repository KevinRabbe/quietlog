package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.ReminderStatus
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository

class CompleteReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: Long) {
        val reminder = repository.getReminderById(id)
        if (reminder != null) {
            repository.updateReminder(
                reminder.copy(
                    status = ReminderStatus.COMPLETED,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
