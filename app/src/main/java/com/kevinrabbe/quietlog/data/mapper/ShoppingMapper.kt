package com.kevinrabbe.quietlog.data.mapper

import com.kevinrabbe.quietlog.data.local.ShoppingItemEntity
import com.kevinrabbe.quietlog.data.local.ShoppingCategoryEntity
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory

fun ShoppingItemEntity.toDomain(): ShoppingListItem = ShoppingListItem(
    id = id,
    title = title,
    isChecked = isChecked,
    quantity = quantity,
    categoryId = categoryId,
    createdAt = createdAt
)

fun ShoppingListItem.toEntity(): ShoppingItemEntity = ShoppingItemEntity(
    id = id,
    title = title,
    isChecked = isChecked,
    quantity = quantity,
    categoryId = categoryId,
    createdAt = createdAt
)

fun ShoppingCategoryEntity.toDomain(): ShoppingCategory = ShoppingCategory(
    id = id,
    name = name,
    sortOrder = sortOrder,
    isCollapsed = isCollapsed,
    isDefault = isDefault,
    createdAt = createdAt
)

fun ShoppingCategory.toEntity(): ShoppingCategoryEntity = ShoppingCategoryEntity(
    id = id,
    name = name,
    sortOrder = sortOrder,
    isCollapsed = isCollapsed,
    isDefault = isDefault,
    createdAt = createdAt
)
