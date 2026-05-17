package com.kevinrabbe.quietlog.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kevinrabbe.quietlog.domain.model.GameEvent
import com.kevinrabbe.quietlog.domain.model.NotificationMode

class GameEventScheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(event: GameEvent) {
        val intent = Intent(context, GameEventNotificationReceiver::class.java).apply {
            putExtra(GameEventNotificationReceiver.EXTRA_EVENT_ID, event.id)
            putExtra(GameEventNotificationReceiver.EXTRA_EVENT_TITLE, event.title)
            putExtra(GameEventNotificationReceiver.EXTRA_EVENT_NOTES, event.notes)
            putExtra(GameEventNotificationReceiver.EXTRA_EVENT_SILENT, event.notificationMode == NotificationMode.SILENT)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (event.id * 10).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate time minus reminder offset
        val time = event.timestamp - (event.reminderOffset * 60_000L)

        // Only schedule if time is in the future
        if (time > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
    }

    fun cancel(eventId: Long) {
        val intent = Intent(context, GameEventNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (eventId * 10).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
