package com.kevinrabbe.quietlog.domain.repository

interface ShoppingReminderScheduler {
    fun schedule(delayMillis: Long)
    fun cancel()
}
