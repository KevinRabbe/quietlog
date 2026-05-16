package com.kevinrabbe.quietlog.core.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.kevinrabbe.quietlog.QuietLogApplication
import com.kevinrabbe.quietlog.feature.reminders.ReminderViewModel

/**
 * Factory to create ViewModels with dependencies from [AppContainer].
 */
val ViewModelProvider.Factory.Companion.AppFactory: ViewModelProvider.Factory
    get() = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as QuietLogApplication
            val container = application.container

            return when (modelClass) {
                ReminderViewModel::class.java -> {
                    ReminderViewModel(
                        observeRemindersUseCase = container.observeRemindersUseCase,
                        createReminderUseCase = container.createReminderUseCase,
                        completeReminderUseCase = container.completeReminderUseCase,
                        deleteReminderUseCase = container.deleteReminderUseCase
                    ) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
