package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import com.kevinrabbe.quietlog.domain.repository.GameEventScheduler

class CreateGameEventUseCase(
    private val repository: GameEventRepository,
    private val scheduler: GameEventScheduler
) {
    suspend operator fun invoke(event: GameEvent) {
        val id = repository.insertEventAndGetId(event)
        scheduler.schedule(event.copy(id = id))
    }
}
