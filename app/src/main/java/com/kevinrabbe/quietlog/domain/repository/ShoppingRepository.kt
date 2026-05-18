package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun observeItems(): Flow<List<ShoppingListItem>>
    suspend fun insertItem(item: ShoppingListItem)
    suspend fun updateItem(item: ShoppingListItem)
    suspend fun deleteItem(id: Long)
    suspend fun deleteCheckedItems()

    // Category operations
    fun observeCategories(): Flow<List<ShoppingCategory>>
    suspend fun insertCategory(category: ShoppingCategory)
    suspend fun updateCategory(category: ShoppingCategory)
    suspend fun deleteCategory(id: Long)
    suspend fun setDefaultCategory(id: Long)
}
