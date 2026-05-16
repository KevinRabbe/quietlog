package com.kevinrabbe.quietlog.data.mapper

import com.kevinrabbe.quietlog.data.local.GameEventEntity
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventStatus
import com.kevinrabbe.quietlog.domain.model.GameEventType

fun GameEventEntity.toDomain(): GameEvent = GameEvent(
    id = id,
    title = title,
    eventType = GameEventType.valueOf(eventType),
    timestamp = timestamp,
    notes = notes,
    status = GameEventStatus.valueOf(status),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun GameEvent.toEntity(): GameEventEntity = GameEventEntity(
    id = id,
    title = title,
    eventType = eventType.name,
    timestamp = timestamp,
    notes = notes,
    status = status.name,
    createdAt = createdAt,
    updatedAt = updatedAt
)
