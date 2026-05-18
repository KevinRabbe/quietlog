package com.kevinrabbe.quietlog.feature.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.R
import com.kevinrabbe.quietlog.core.util.AppFactory
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    viewModel: ShoppingViewModel = viewModel(factory = ViewModelProvider.Factory.AppFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val groupedItems = remember(uiState.items, uiState.categories) {
        val map = mutableMapOf<Long?, List<ShoppingListItem>>()
        uiState.items.forEach { item ->
            val list = map[item.categoryId] ?: emptyList()
            map[item.categoryId] = list + item
        }
        map
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ShoppingEvent.ToggleAddCategoryDialog(true)) }) {
                        Icon(Icons.Default.CreateNewFolder, contentDescription = "Add Category")
                    }
                    if (uiState.items.any { it.isChecked }) {
                        TextButton(onClick = { viewModel.onEvent(ShoppingEvent.ClearChecked) }) {
                            Text("Clear Completed")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Add Item Input Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.newItemTitle,
                        onValueChange = { viewModel.onEvent(ShoppingEvent.TitleChanged(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add item...") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.onEvent(ShoppingEvent.AddItem) })
                    )

                    // Quick category selector dropdown
                    var expanded by remember { mutableStateOf(false) }
                    val selectedCategoryName = uiState.categories.find { it.id == uiState.selectedNewItemCategoryId }?.name ?: "None"
                    
                    Box {
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text(selectedCategoryName)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            uiState.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = category.name,
                                            fontWeight = if (category.isDefault) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        viewModel.onEvent(ShoppingEvent.NewItemCategoryChanged(category.id))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { viewModel.onEvent(ShoppingEvent.AddItem) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item")
                    }
                }

                if (uiState.items.isEmpty() && !uiState.isLoading) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your shopping list is empty",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.categories.forEach { category ->
                            val categoryItems = groupedItems[category.id] ?: emptyList()

                            item(key = "cat_${category.id}") {
                                CategoryHeaderItem(
                                    category = category,
                                    checkedCount = categoryItems.count { it.isChecked },
                                    totalCount = categoryItems.size,
                                    onToggleCollapse = { viewModel.onEvent(ShoppingEvent.ToggleCategoryCollapsed(category)) },
                                    onReorderUp = { viewModel.onEvent(ShoppingEvent.ReorderCategory(category, true)) },
                                    onReorderDown = { viewModel.onEvent(ShoppingEvent.ReorderCategory(category, false)) },
                                    onSetDefault = { viewModel.onEvent(ShoppingEvent.SetDefaultCategory(category.id)) },
                                    onDelete = { viewModel.onEvent(ShoppingEvent.DeleteCategory(category.id)) }
                                )
                            }

                            if (!category.isCollapsed) {
                                items(categoryItems, key = { "item_${it.id}" }) { item ->
                                    ShoppingListItemRow(
                                        item = item,
                                        onToggle = { viewModel.onEvent(ShoppingEvent.ToggleItem(item)) },
                                        onEdit = { viewModel.onEvent(ShoppingEvent.StartEditingItem(item)) },
                                        onDelete = { viewModel.onEvent(ShoppingEvent.DeleteItem(item.id)) }
                                    )
                                }
                            }
                        }

                        // Fallback uncategorized items
                        val uncategorized = groupedItems[null] ?: emptyList()
                        if (uncategorized.isNotEmpty()) {
                            item(key = "cat_uncategorized") {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Uncategorized (${uncategorized.count { it.isChecked }}/${uncategorized.size})",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            items(uncategorized, key = { "item_${it.id}" }) { item ->
                                ShoppingListItemRow(
                                    item = item,
                                    onToggle = { viewModel.onEvent(ShoppingEvent.ToggleItem(item)) },
                                    onEdit = { viewModel.onEvent(ShoppingEvent.StartEditingItem(item)) },
                                    onDelete = { viewModel.onEvent(ShoppingEvent.DeleteItem(item.id)) }
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Dialog for Add Custom Category
            if (uiState.isAddingCategory) {
                AlertDialog(
                    onDismissRequest = { viewModel.onEvent(ShoppingEvent.ToggleAddCategoryDialog(false)) },
                    title = { Text("Add Category") },
                    text = {
                        TextField(
                            value = uiState.newCategoryName,
                            onValueChange = { viewModel.onEvent(ShoppingEvent.NewCategoryNameChanged(it)) },
                            placeholder = { Text("Category name...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.onEvent(ShoppingEvent.AddCategory) },
                            enabled = uiState.newCategoryName.isNotBlank()
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onEvent(ShoppingEvent.ToggleAddCategoryDialog(false)) }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Edit Item Dialog
            uiState.editingItem?.let { item ->
                EditItemDialog(
                    item = item,
                    categories = uiState.categories,
                    onDismiss = { viewModel.onEvent(ShoppingEvent.StopEditingItem) },
                    onSave = { title, quantity, categoryId ->
                        viewModel.onEvent(ShoppingEvent.SaveEditedItem(title, quantity, categoryId))
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryHeaderItem(
    category: ShoppingCategory,
    checkedCount: Int,
    totalCount: Int,
    onToggleCollapse: () -> Unit,
    onReorderUp: () -> Unit,
    onReorderDown: () -> Unit,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onToggleCollapse) {
                Icon(
                    imageVector = if (category.isCollapsed) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (category.isCollapsed) "Expand" else "Collapse"
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    if (category.isDefault) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Default Favorite Category",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = "$checkedCount / $totalCount completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }

            // Quick Arrow Controls
            IconButton(onClick = onReorderUp) {
                Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = onReorderDown) {
                Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", modifier = Modifier.size(20.dp))
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    if (!category.isDefault) {
                        DropdownMenuItem(
                            text = { Text("Set as Default") },
                            onClick = {
                                onSetDefault()
                                menuExpanded = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Delete Category") },
                        onClick = {
                            onDelete()
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingListItemRow(
    item: ShoppingListItem,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() }
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                    )
                    if (item.quantity > 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        SuggestionChip(
                            onClick = {},
                            label = { Text("x${item.quantity}") },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemDialog(
    item: ShoppingListItem,
    categories: List<ShoppingCategory>,
    onDismiss: () -> Unit,
    onSave: (String, Int, Long?) -> Unit
) {
    var title by remember { mutableStateOf(item.title) }
    var quantity by remember { mutableIntStateOf(item.quantity) }
    var selectedCategoryId by remember { mutableStateOf(item.categoryId) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Quantity selector row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Quantity", style = MaterialTheme.typography.bodyLarge)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledIconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("-", style = MaterialTheme.typography.titleMedium)
                        }
                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.widthIn(min = 24.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        FilledIconButton(
                            onClick = { quantity++ },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("+", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                // Category selector
                val currentCategoryName = categories.find { it.id == selectedCategoryId }?.name ?: "None"
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    TextField(
                        value = currentCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                selectedCategoryId = null
                                categoryDropdownExpanded = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, quantity, selectedCategoryId) },
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
