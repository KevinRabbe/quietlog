package com.kevinrabbe.quietlog.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevinrabbe.quietlog.data.local.ReminderDao
import com.kevinrabbe.quietlog.data.local.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuietLogDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: QuietLogDatabase? = null

        fun getInstance(context: Context): QuietLogDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuietLogDatabase::class.java,
                    "quietlog_database"
                ).build().also { instance = it }
            }
        }
    }
}
