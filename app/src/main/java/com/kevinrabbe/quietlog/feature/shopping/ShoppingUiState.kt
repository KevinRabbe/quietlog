package com.kevinrabbe.quietlog.feature.shopping

import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory

data class ShoppingUiState(
    val items: List<ShoppingListItem> = emptyList(),
    val categories: List<ShoppingCategory> = emptyList(),
    val isLoading: Boolean = false,
    val newItemTitle: String = "",
    val selectedNewItemCategoryId: Long? = null,
    val editingItem: ShoppingListItem? = null,
    val isAddingCategory: Boolean = false,
    val newCategoryName: String = ""
)

sealed interface ShoppingEvent {
    data class TitleChanged(val title: String) : ShoppingEvent
    data class NewItemCategoryChanged(val categoryId: Long?) : ShoppingEvent
    data object AddItem : ShoppingEvent
    data class ToggleItem(val item: ShoppingListItem) : ShoppingEvent
    data class DeleteItem(val id: Long) : ShoppingEvent
    data object ClearChecked : ShoppingEvent

    // Category Events
    data class ToggleCategoryCollapsed(val category: ShoppingCategory) : ShoppingEvent
    data class ReorderCategory(val category: ShoppingCategory, val up: Boolean) : ShoppingEvent
    data class SetDefaultCategory(val categoryId: Long) : ShoppingEvent
    data class ToggleAddCategoryDialog(val show: Boolean) : ShoppingEvent
    data class NewCategoryNameChanged(val name: String) : ShoppingEvent
    data object AddCategory : ShoppingEvent
    data class DeleteCategory(val categoryId: Long) : ShoppingEvent

    // Edit Item Events
    data class StartEditingItem(val item: ShoppingListItem) : ShoppingEvent
    data object StopEditingItem : ShoppingEvent
    data class SaveEditedItem(val title: String, val quantity: Int, val categoryId: Long?) : ShoppingEvent
}
