package com.kevinrabbe.quietlog.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(EXTRA_REMINDER_ID, -1L)
        val title = intent.getStringExtra(EXTRA_REMINDER_TITLE) ?: "Reminder"
        val notes = intent.getStringExtra(EXTRA_REMINDER_NOTES) ?: ""

        if (reminderId != -1L) {
            val notificationHelper = NotificationHelper(context)
            notificationHelper.showReminderNotification(reminderId, title, notes)
        }
    }

    companion object {
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        const val EXTRA_REMINDER_TITLE = "extra_reminder_title"
        const val EXTRA_REMINDER_NOTES = "extra_reminder_notes"
    }
}
