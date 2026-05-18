package com.kevinrabbe.quietlog.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_categories")
data class ShoppingCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "sort_order") val sortOrder: Int,
    @ColumnInfo(name = "is_collapsed") val isCollapsed: Boolean = false,
    @ColumnInfo(name = "is_default") val isDefault: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
