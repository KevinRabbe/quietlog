package com.kevinrabbe.quietlog.data.mapper

import com.kevinrabbe.quietlog.data.local.ReminderEntity
import com.kevinrabbe.quietlog.domain.model.NotificationMode
import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.model.Reminderstatus
import com.kevinrabbe.quietlog.domain.model.RepeatRule

fun ReminderEntity.toDomain(): Reminder = Reminder(
    id = id,
    title = title,
    notes = notes,
    dateTimeMillis = dateTimeMillis,
    repeatRule = RepeatRule.valueOf(repeatRule),
    notificationMode = NotificationMode.valueOf(notificationMode),
    status = ReminderStatus.valueOf(status),
    phoneNumber = phoneNumber,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Reminder.toEntity(): ReminderEntity = ReminderEntity(
    id = id,
    title = title,
    notes = notes,
    dateTimeMillis = dateTimeMillis,
    repeatRule = repeatRule.name,
    notificationMode = notificationMode.name,
    status = status.name,
    phoneNumber = phoneNumber,
    createdAt = createdAt,
    updatedAt = updatedAt
)
