package com.kevinrabbe.quietlog.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "is_checked") val isChecked: Boolean,
    @ColumnInfo(name = "quantity", defaultValue = "1") val quantity: Int = 1,
    @ColumnInfo(name = "category_id") val categoryId: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
