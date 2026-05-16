package com.kevinrabbe.quietlog.domain.model

/**
 * Full reminder domain model.
 * dateTimeMillis is epoch milliseconds for the scheduled alarm time.
 */
data class Reminder(
    val id: Long = 0,
    val title: String,
    val notes: String = "",
    val dateTimeMillis: Long,
    val repeatRule: RepeatRule = RepeatRule.NONE,
    val notificationMode: NotificationMode = NotificationMode.NOTIFICATION,
    val status: ReminderStatus = ReminderStatus.ACTIVE,
    val phoneNumber: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
