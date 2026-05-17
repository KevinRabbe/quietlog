package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.GameEvent
import kotlinx.coroutines.flow.Flow

interface GameEventRepository {
    fun observeEvents(): Flow<List<GameEvent>>
    suspend fun getEventById(id: Long): GameEvent?
    suspend fun insertEvent(event: GameEvent)
    suspend fun insertEventAndGetId(event: GameEvent): Long
    suspend fun updateEvent(event: GameEvent)
    suspend fun deleteEvent(id: Long)
}
