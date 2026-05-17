package com.kevinrabbe.quietlog.data.mapper

import com.kevinrabbe.quietlog.data.local.GameEventEntity
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.GameEventStatus
import com.kevinrabbe.quietlog.domain.model.GameEventType
import com.kevinrabbe.quietlog.domain.model.NotificationMode
import com.kevinrabbe.quietlog.domain.model.RepeatRule

fun GameEventEntity.toDomain(): GameEvent = GameEvent(
    id = id,
    title = title,
    eventType = GameEventType.valueOf(eventType),
    timestamp = timestamp,
    notes = notes,
    status = GameEventStatus.valueOf(status),
    packageName = packageName,
    reminderOffset = reminderOffset,
    repeatRule = RepeatRule.valueOf(repeatRule),
    notificationMode = NotificationMode.valueOf(notificationMode),
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
    packageName = packageName,
    reminderOffset = reminderOffset,
    repeatRule = repeatRule.name,
    notificationMode = notificationMode.name,
    createdAt = createdAt,
    updatedAt = updatedAt
)
