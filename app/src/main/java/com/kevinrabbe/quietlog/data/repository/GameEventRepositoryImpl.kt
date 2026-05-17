package com.kevinrabbe.quietlog.data.repository

import com.kevinrabbe.quietlog.data.local.GameEventDao
import com.kevinrabbe.quietlog.data.mapper.toDomain
import com.kevinrabbe.quietlog.data.mapper.toEntity
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameEventRepositoryImpl(
    private val dao: GameEventDao
) : GameEventRepository {

    override fun observeEvents(): Flow<List<GameEvent>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getEventById(id: Long): GameEvent? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun insertEvent(event: GameEvent) {
        dao.insert(event.toEntity())
    }

    override suspend fun insertEventAndGetId(event: GameEvent): Long {
        return dao.insert(event.toEntity())
    }

    override suspend fun updateEvent(event: GameEvent) {
        dao.update(event.toEntity())
    }

    override suspend fun deleteEvent(id: Long) {
        dao.deleteById(id)
    }
}
