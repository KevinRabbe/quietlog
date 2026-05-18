package com.kevinrabbe.quietlog.domain.usecase

import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import kotlinx.coroutines.flow.Flow

class ObserveGameEventsUseCase(
    private val repository: GameEventRepository
) {
    operator fun invoke(): Flow<List<GameEvent>> = repository.observeEvents()
}
