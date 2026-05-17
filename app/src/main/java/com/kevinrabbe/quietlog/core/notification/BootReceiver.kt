package com.kevinrabbe.quietlog.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevinrabbe.quietlog.QuietLogApplication
import com.kevinrabbe.quietlog.domain.model.ReminderStatus
import com.kevinrabbe.quietlog.domain.model.GameEventStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val application = context.applicationContext as QuietLogApplication
            val container = application.container

            scope.launch {
                val now = System.currentTimeMillis()

                // Reschedule Reminders
                val reminders = container.reminderRepository.observeReminders().first()
                reminders.filter { it.status == ReminderStatus.ACTIVE && it.dateTimeMillis > now }
                    .forEach { reminder ->
                        container.reminderScheduler.schedule(reminder)
                    }

                // Reschedule Game Events
                val gameEvents = container.gameEventRepository.observeEvents().first()
                gameEvents.filter { it.status == GameEventStatus.UPCOMING || it.status == GameEventStatus.ONGOING }
                    .filter { it.timestamp > now }
                    .forEach { event ->
                        container.gameEventScheduler.schedule(event)
                    }
            }
        }
    }
}
