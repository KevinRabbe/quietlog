package com.kevinrabbe.quietlog.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevinrabbe.quietlog.data.local.ReminderDao
import com.kevinrabbe.quietlog.data.local.ReminderEntity
import com.kevinrabbe.quietlog.data.local.ShoppingDao
import com.kevinrabbe.quietlog.data.local.ShoppingItemEntity
import com.kevinrabbe.quietlog.data.local.GameEventDao
import com.kevinrabbe.quietlog.data.local.GameEventEntity

@Database(
    entities = [
        ReminderEntity::class,
        ShoppingItemEntity::class,
        GameEventEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class QuietLogDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun gameEventDao(): GameEventDao

    companion object {
        @Volatile
        private var instance: QuietLogDatabase? = null

        fun getInstance(context: Context): QuietLogDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuietLogDatabase::class.java,
                    "quietlog_database"
                ).fallbackToDestructiveMigration()
                 .build().also { instance = it }
            }
        }
    }
}
