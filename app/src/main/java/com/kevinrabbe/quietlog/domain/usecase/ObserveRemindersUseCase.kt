package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class ObserveRemindersUseCase(
    private val repository: ReminderRepository
) {
    operator fun invoke(): Flow<List<Reminder>> = repository.observeReminders()
}
