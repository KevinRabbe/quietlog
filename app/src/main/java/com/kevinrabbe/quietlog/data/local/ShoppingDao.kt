package com.kevinrabbe.quietlog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY is_checked ASC, created_at DESC")
    fun observeAll(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ShoppingItemEntity): Long

    @Update
    suspend fun update(entity: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM shopping_items WHERE is_checked = 1")
    suspend fun deleteChecked()

    // --- Category DAO methods ---

    @Query("SELECT * FROM shopping_categories ORDER BY sort_order ASC")
    fun observeCategories(): Flow<List<ShoppingCategoryEntity>>

    @Query("SELECT * FROM shopping_categories ORDER BY sort_order ASC")
    suspend fun getCategories(): List<ShoppingCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(entity: ShoppingCategoryEntity): Long

    @Update
    suspend fun updateCategory(entity: ShoppingCategoryEntity)

    @Query("DELETE FROM shopping_categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)

    @Query("UPDATE shopping_items SET category_id = null WHERE category_id = :categoryId")
    suspend fun unlinkItemsFromCategory(categoryId: Long)

    @Query("UPDATE shopping_categories SET is_default = 0")
    suspend fun clearDefaultCategory()
}
