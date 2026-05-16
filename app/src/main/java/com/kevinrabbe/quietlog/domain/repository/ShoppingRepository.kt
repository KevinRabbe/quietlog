package com.kevinrabbe.quietlog.domain.repository

import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun observeItems(): Flow<List<ShoppingListItem>>
    suspend fun insertItem(item: ShoppingListItem)
    suspend fun updateItem(item: ShoppingListItem)
    suspend fun deleteItem(id: Long)
    suspend fun deleteCheckedItems()
}
