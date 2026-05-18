package com.kevinrabbe.quietlog.domain.model

data class ShoppingCategory(
    val id: Long = 0,
    val name: String,
    val sortOrder: Int,
    val isCollapsed: Boolean = false,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
