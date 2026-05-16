package com.kevinrabbe.quietlog.feature.shopping

import com.kevinrabbe.quietlog.domain.model.ShoppingListItem

data class ShoppingUiState(
    val items: List<ShoppingListItem> = emptyList(),
    val isLoading: Boolean = false,
    val newItemTitle: String = ""
)

sealed interface ShoppingEvent {
    data class TitleChanged(val title: String) : ShoppingEvent
    data object AddItem : ShoppingEvent
    data class ToggleItem(val item: ShoppingListItem) : ShoppingEvent
    data class DeleteItem(val id: Long) : ShoppingEvent
    data object ClearChecked : ShoppingEvent
}
