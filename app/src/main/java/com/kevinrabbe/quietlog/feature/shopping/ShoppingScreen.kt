package com.kevinrabbe.quietlog.feature.shopping

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kevinrabbe.quietlog.core.util.AppFactory
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    viewModel: ShoppingViewModel = viewModel(factory = ViewModelProvider.Factory.AppFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ShoppingEvent.ClearChecked) }) {
                        Icon(Icons.Default.ClearAll, contentDescription = "Clear Checked")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Input field
            TextField(
                value = uiState.newItemTitle,
                onValueChange = { viewModel.onEvent(ShoppingEvent.TitleChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Add item...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.onEvent(ShoppingEvent.AddItem) })
            )

            if (uiState.items.isEmpty() && !uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("List is empty.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.items, key = { it.id }) { item ->
                        ShoppingItem(
                            item = item,
                            onToggle = { viewModel.onEvent(ShoppingEvent.ToggleItem(item)) },
                            onDelete = { viewModel.onEvent(ShoppingEvent.DeleteItem(item.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingItem(
    item: ShoppingListItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() }
            )
            Text(
                text = item.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (item.isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
