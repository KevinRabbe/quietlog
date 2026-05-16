package com.kevinrabbe.quietlog.domain.model

data class ShoppingListItem(
    val id: Long = 0,
    val title: String,
    val isChecked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
