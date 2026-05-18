package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.GameEvent

interface GameEventScheduler {
    fun schedule(event: GameEvent)
    fun cancel(eventId: Long)
}
