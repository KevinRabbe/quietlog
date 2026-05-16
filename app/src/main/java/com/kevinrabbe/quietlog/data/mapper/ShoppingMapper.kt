package com.kevinrabbe.quietlog.data.mapper

import com.kevinrabbe.quietlog.data.local.ShoppingItemEntity
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem

fun ShoppingItemEntity.toDomain(): ShoppingListItem = ShoppingListItem(
    id = id,
    title = title,
    isChecked = isChecked,
    createdAt = createdAt
)

fun ShoppingListItem.toEntity(): ShoppingItemEntity = ShoppingItemEntity(
    id = id,
    title = title,
    isChecked = isChecked,
    createdAt = createdAt
)
