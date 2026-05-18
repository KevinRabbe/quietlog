package com.kevinrabbe.quietlog.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kevinrabbe.quietlog.QuietLogApplication
import com.kevinrabbe.quietlog.domain.model.GameEventStatus
import com.kevinrabbe.quietlog.domain.model.RepeatRule
import com.kevinrabbe.quietlog.domain.util.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameEventNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra(EXTRA_EVENT_ID, -1L)
        val title = intent.getStringExtra(EXTRA_EVENT_TITLE) ?: "Game Event"
        val notes = intent.getStringExtra(EXTRA_EVENT_NOTES) ?: ""
        val isSilent = intent.getBooleanExtra(EXTRA_EVENT_SILENT, false)

        if (eventId != -1L) {
            val notificationHelper = NotificationHelper(context)
            notificationHelper.showGameEventNotification(eventId, title, notes, isSilent)

            val pendingResult = goAsync()
            val application = context.applicationContext as QuietLogApplication
            val repository = application.container.gameEventRepository
            val scheduler = application.container.gameEventScheduler

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val event = repository.getEventById(eventId)
                    if (event != null) {
                        if (event.repeatRule == RepeatRule.NONE) {
                            // Mark as completed
                            repository.updateEvent(event.copy(status = GameEventStatus.COMPLETED))
                        } else {
                            // Calculate next occurrence
                            val nextTimestamp = TimeUtils.calculateNextOccurrence(event.timestamp, event.repeatRule)
                            val updatedEvent = event.copy(timestamp = nextTimestamp)
                            
                            repository.updateEvent(updatedEvent)
                            scheduler.schedule(updatedEvent)
                        }
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val EXTRA_EVENT_TITLE = "extra_event_title"
        const val EXTRA_EVENT_NOTES = "extra_event_notes"
        const val EXTRA_EVENT_SILENT = "extra_event_silent"
    }
}
