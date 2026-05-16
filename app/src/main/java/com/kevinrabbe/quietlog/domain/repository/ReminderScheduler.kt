package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.Reminder

interface ReminderScheduler {
    fun schedule(reminder: Reminder)
    fun cancel(reminderId: Long)
}
