package com.kevinrabbe.quietlog.core.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevinrabbe.quietlog.QuietLogApplication
import com.kevinrabbe.quietlog.domain.model.ReminderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(EXTRA_REMINDER_ID, -1L)
        if (reminderId == -1L) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(reminderId.toInt())

        val application = context.applicationContext as QuietLogApplication
        val repository = application.container.reminderRepository
        val scheduler = application.container.reminderScheduler

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (intent.action == ACTION_DONE) {
                    val reminder = repository.getReminderById(reminderId)
                    if (reminder != null) {
                        repository.updateReminder(reminder.copy(status = ReminderStatus.COMPLETED))
                        scheduler.cancel(reminderId)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val ACTION_DONE = "com.kevinrabbe.quietlog.ACTION_DONE"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
    }
}
