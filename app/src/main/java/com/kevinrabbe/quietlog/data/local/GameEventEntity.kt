package com.kevinrabbe.quietlog.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_events")
data class GameEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "event_type") val eventType: String,
    val timestamp: Long,
    val notes: String,
    val status: String,
    @ColumnInfo(name = "package_name") val packageName: String?,
    @ColumnInfo(name = "reminder_offset") val reminderOffset: Int,
    @ColumnInfo(name = "repeat_rule") val repeatRule: String,
    @ColumnInfo(name = "notification_mode") val notificationMode: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
