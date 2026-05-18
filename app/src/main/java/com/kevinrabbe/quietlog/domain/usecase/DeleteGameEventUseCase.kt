package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import com.kevinrabbe.quietlog.domain.repository.GameEventScheduler

class DeleteGameEventUseCase(
    private val repository: GameEventRepository,
    private val scheduler: GameEventScheduler
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteEvent(id)
        scheduler.cancel(id)
    }
}
