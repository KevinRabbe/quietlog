package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.GameEvent
import kotlinx.coroutines.flow.Flow

interface GameEventRepository {
    fun observeEvents(): Flow<List<GameEvent>>
    suspend fun insertEvent(event: GameEvent)
    suspend fun updateEvent(event: GameEvent)
    suspend fun deleteEvent(id: Long)
}
