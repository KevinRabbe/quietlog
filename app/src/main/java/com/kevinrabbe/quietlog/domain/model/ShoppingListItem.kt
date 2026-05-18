package com.kevinrabbe.quietlog.domain.model

data class ShoppingListItem(
    val id: Long = 0,
    val title: String,
    val isChecked: Boolean = false,
    val quantity: Int = 1,
    val categoryId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
