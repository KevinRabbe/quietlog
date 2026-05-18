package com.kevinrabbe.quietlog.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kevinrabbe.quietlog.domain.repository.ShoppingReminderScheduler

class AlarmShoppingReminderScheduler(
    private val context: Context
) : ShoppingReminderScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(delayMillis: Long) {
        val intent = Intent(context, ShoppingReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ShoppingReminderReceiver.SHOPPING_NOTIFICATION_ID.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + delayMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        }
    }

    override fun cancel() {
        val intent = Intent(context, ShoppingReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ShoppingReminderReceiver.SHOPPING_NOTIFICATION_ID.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
