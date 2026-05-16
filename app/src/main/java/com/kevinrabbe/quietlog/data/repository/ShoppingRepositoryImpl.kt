package com.kevinrabbe.quietlog.data.repository

import com.kevinrabbe.quietlog.data.local.ShoppingDao
import com.kevinrabbe.quietlog.data.mapper.toDomain
import com.kevinrabbe.quietlog.data.mapper.toEntity
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingRepositoryImpl(
    private val dao: ShoppingDao
) : ShoppingRepository {

    override fun observeItems(): Flow<List<ShoppingListItem>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertItem(item: ShoppingListItem) {
        dao.insert(item.toEntity())
    }

    override suspend fun updateItem(item: ShoppingListItem) {
        dao.update(item.toEntity())
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteCheckedItems() {
        dao.deleteChecked()
    }
}
