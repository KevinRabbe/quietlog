package com.kevinrabbe.quietlog.data.repository

import com.kevinrabbe.quietlog.data.local.ShoppingDao
import com.kevinrabbe.quietlog.data.mapper.toDomain
import com.kevinrabbe.quietlog.data.mapper.toEntity
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory
import com.kevinrabbe.quietlog.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

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

    // --- Category operations implementation ---

    override fun observeCategories(): Flow<List<ShoppingCategory>> {
        return dao.observeCategories()
            .onStart {
                ensureDefaultCategoriesExist()
            }
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    private suspend fun ensureDefaultCategoriesExist() {
        withContext(Dispatchers.IO) {
            val existing = dao.getCategories()
            if (existing.isEmpty()) {
                val defaults = listOf(
                    "Food",
                    "Drinks",
                    "Frozen",
                    "Hygiene",
                    "Pets",
                    "Household",
                    "Other"
                )
                defaults.forEachIndexed { index, name ->
                    dao.insertCategory(
                        com.kevinrabbe.quietlog.data.local.ShoppingCategoryEntity(
                            name = name,
                            sortOrder = index,
                            isDefault = name == "Food" // Default favorite initially
                        )
                    )
                }
            }
        }
    }

    override suspend fun insertCategory(category: ShoppingCategory) {
        dao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: ShoppingCategory) {
        dao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(id: Long) {
        dao.unlinkItemsFromCategory(id)
        dao.deleteCategoryById(id)
    }

    override suspend fun setDefaultCategory(id: Long) {
        dao.clearDefaultCategory()
        val categories = dao.getCategories()
        categories.forEach { cat ->
            if (cat.id == id) {
                dao.updateCategory(cat.copy(isDefault = true))
            }
        }
    }
}
