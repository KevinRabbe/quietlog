package com.kevinrabbe.quietlog.domain.model

enum class GameEventType {
    RAID,
    SPAWN,
    MATCH,
    TOURNAMENT,
    OTHER
}

enum class GameEventStatus {
    UPCOMING,
    ONGOING,
    COMPLETED,
    CANCELLED
}

data class GameEvent(
    val id: Long = 0,
    val title: String,
    val eventType: GameEventType = GameEventType.OTHER,
    val timestamp: Long,
    val notes: String = "",
    val status: GameEventStatus = GameEventStatus.UPCOMING,
    val packageName: String? = null,
    val reminderOffset: Int = 0, // in minutes
    val repeatRule: RepeatRule = RepeatRule.NONE,
    val notificationMode: NotificationMode = NotificationMode.NOTIFICATION,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
