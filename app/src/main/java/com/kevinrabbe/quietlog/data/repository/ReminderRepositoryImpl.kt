package com.kevinrabbe.quietlog.data.repository

import com.kevinrabbe.quietlog.data.local.ReminderDao
import com.kevinrabbe.quietlog.data.mapper.toDomain
import com.kevinrabbe.quietlog.data.mapper.toEntity
import com.kevinrabbe.quietlog.domain.model.Reminder
import com.kevinrabbe.quietlog.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(
    private val dao: ReminderDao
) : ReminderRepository {

    override fun observeReminders(): Flow<List<Reminder>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getReminderById(id: Long): Reminder? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun createReminder(reminder: Reminder): Long {
        return dao.insert(reminder.toEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        dao.update(reminder.toEntity())
    }

    override suspend fun deleteReminder(id: Long) {
        dao.deleteById(id)
    }
}
