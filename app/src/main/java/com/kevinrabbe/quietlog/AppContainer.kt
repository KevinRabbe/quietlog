package com.kevinrabbe.quietlog

import android.content.Context
import com.kevinrabbe.quietlog.core.database.QuietLogDatabase
import com.kevinrabbe.quietlog.core.notification.AlarmReminderScheduler
import com.kevinrabbe.quietlog.data.repository.ReminderRepositoryImpl
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import com.kevinrabbe.quietlog.domain.repository.ReminderScheduler
import com.kevinrabbe.quietlog.domain.repository.SettingsRepository
import com.kevinrabbe.quietlog.domain.repository.ShoppingRepository
import com.kevinrabbe.quietlog.domain.repository.ShoppingReminderScheduler
import com.kevinrabbe.quietlog.domain.repository.GameEventRepository
import com.kevinrabbe.quietlog.domain.usecase.CompleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.CreateReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.DeleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.ObserveRemindersUseCase
import com.kevinrabbe.quietlog.domain.usecase.ObserveGameEventsUseCase
import com.kevinrabbe.quietlog.domain.usecase.CreateGameEventUseCase
import com.kevinrabbe.quietlog.domain.usecase.DeleteGameEventUseCase

/**
 * Simple dependency container for the app.
 */
class AppContainer(context: Context) {

    private val database: QuietLogDatabase by lazy {
        QuietLogDatabase.getInstance(context)
    }

    val reminderRepository: ReminderRepository by lazy {
        ReminderRepositoryImpl(database.reminderDao())
    }

    val reminderScheduler: ReminderScheduler by lazy {
        AlarmReminderScheduler(context)
    }

    val settingsRepository: SettingsRepository by lazy {
        com.kevinrabbe.quietlog.data.repository.SettingsRepositoryImpl(context)
    }

    val shoppingRepository: ShoppingRepository by lazy {
        com.kevinrabbe.quietlog.data.repository.ShoppingRepositoryImpl(database.shoppingDao())
    }

    val shoppingReminderScheduler: ShoppingReminderScheduler by lazy {
        com.kevinrabbe.quietlog.core.notification.AlarmShoppingReminderScheduler(context)
    }

    val gameEventRepository: GameEventRepository by lazy {
        com.kevinrabbe.quietlog.data.repository.GameEventRepositoryImpl(database.gameEventDao())
    }

    val gameEventScheduler: com.kevinrabbe.quietlog.domain.repository.GameEventScheduler by lazy {
        com.kevinrabbe.quietlog.core.notification.AlarmGameEventScheduler(context)
    }

    val observeRemindersUseCase: ObserveRemindersUseCase by lazy {
        ObserveRemindersUseCase(reminderRepository)
    }

    val createReminderUseCase: CreateReminderUseCase by lazy {
        CreateReminderUseCase(reminderRepository, reminderScheduler)
    }

    val completeReminderUseCase: CompleteReminderUseCase by lazy {
        CompleteReminderUseCase(reminderRepository, reminderScheduler)
    }

    val deleteReminderUseCase: DeleteReminderUseCase by lazy {
        DeleteReminderUseCase(reminderRepository, reminderScheduler)
    }

    val observeGameEventsUseCase: ObserveGameEventsUseCase by lazy {
        ObserveGameEventsUseCase(gameEventRepository)
    }

    val createGameEventUseCase: CreateGameEventUseCase by lazy {
        CreateGameEventUseCase(gameEventRepository, gameEventScheduler)
    }

    val deleteGameEventUseCase: DeleteGameEventUseCase by lazy {
        DeleteGameEventUseCase(gameEventRepository, gameEventScheduler)
    }
}
