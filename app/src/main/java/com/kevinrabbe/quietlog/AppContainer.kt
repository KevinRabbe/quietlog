package com.kevinrabbe.quietlog

import android.content.Context
import com.kevinrabbe.quietlog.core.database.QuietLogDatabase
import com.kevinrabbe.quietlog.data.repository.ReminderRepositoryImpl
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import com.kevinrabbe.quietlog.domain.usecase.CompleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.CreateReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.DeleteReminderUseCase
import com.kevinrabbe.quietlog.domain.usecase.ObserveRemindersUseCase

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

    val observeRemindersUseCase: ObserveRemindersUseCase by lazy {
        ObserveRemindersUseCase(reminderRepository)
    }

    val createReminderUseCase: CreateReminderUseCase by lazy {
        CreateReminderUseCase(reminderRepository)
    }

    val completeReminderUseCase: CompleteReminderUseCase by lazy {
        CompleteReminderUseCase(reminderRepository)
    }

    val deleteReminderUseCase: DeleteReminderUseCase by lazy {
        DeleteReminderUseCase(reminderRepository)
    }
}
