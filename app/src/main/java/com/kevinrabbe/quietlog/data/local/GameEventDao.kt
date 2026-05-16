package com.kevinrabbe.quietlog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameEventDao {
    @Query("SELECT * FROM game_events ORDER BY timestamp ASC")
    fun observeAll(): Flow<List<GameEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GameEventEntity): Long

    @Update
    suspend fun update(entity: GameEventEntity)

    @Query("DELETE FROM game_events WHERE id = :id")
    suspend fun deleteById(id: Long)
}
