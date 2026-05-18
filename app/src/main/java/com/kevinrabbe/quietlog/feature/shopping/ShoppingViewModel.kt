package com.kevinrabbe.quietlog.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
import com.kevinrabbe.quietlog.domain.model.ShoppingCategory
import com.kevinrabbe.quietlog.domain.repository.SettingsRepository
import com.kevinrabbe.quietlog.domain.repository.ShoppingRepository
import com.kevinrabbe.quietlog.domain.repository.ShoppingReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val repository: ShoppingRepository,
    private val settingsRepository: SettingsRepository,
    private val reminderScheduler: ShoppingReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        observeItems()
        observeCategories()
    }

    private fun observeItems() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.observeItems().collect { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            repository.observeCategories().collect { categories ->
                val defaultCat = categories.find { it.isDefault }
                _uiState.update { state ->
                    state.copy(
                        categories = categories,
                        selectedNewItemCategoryId = state.selectedNewItemCategoryId ?: defaultCat?.id
                    )
                }
            }
        }
    }

    fun onEvent(event: ShoppingEvent) {
        when (event) {
            is ShoppingEvent.TitleChanged -> {
                _uiState.update { it.copy(newItemTitle = event.title) }
            }
            is ShoppingEvent.NewItemCategoryChanged -> {
                _uiState.update { it.copy(selectedNewItemCategoryId = event.categoryId) }
            }
            is ShoppingEvent.AddItem -> {
                addItem()
            }
            is ShoppingEvent.ToggleItem -> {
                viewModelScope.launch {
                    val updatedItem = event.item.copy(isChecked = !event.item.isChecked)
                    repository.updateItem(updatedItem)
                    handleReminderUpdate()
                }
            }
            is ShoppingEvent.DeleteItem -> {
                viewModelScope.launch {
                    repository.deleteItem(event.id)
                    handleReminderUpdate()
                }
            }
            is ShoppingEvent.ClearChecked -> {
                viewModelScope.launch {
                    repository.deleteCheckedItems()
                    reminderScheduler.cancel()
                }
            }

            // Category Events
            is ShoppingEvent.ToggleCategoryCollapsed -> {
                viewModelScope.launch {
                    repository.updateCategory(event.category.copy(isCollapsed = !event.category.isCollapsed))
                }
            }
            is ShoppingEvent.ReorderCategory -> {
                reorderCategory(event.category, event.up)
            }
            is ShoppingEvent.SetDefaultCategory -> {
                viewModelScope.launch {
                    repository.setDefaultCategory(event.categoryId)
                }
            }
            is ShoppingEvent.ToggleAddCategoryDialog -> {
                _uiState.update { it.copy(isAddingCategory = event.show, newCategoryName = "") }
            }
            is ShoppingEvent.NewCategoryNameChanged -> {
                _uiState.update { it.copy(newCategoryName = event.name) }
            }
            is ShoppingEvent.AddCategory -> {
                addCategory()
            }
            is ShoppingEvent.DeleteCategory -> {
                deleteCategory(event.categoryId)
            }

            // Edit Item Events
            is ShoppingEvent.StartEditingItem -> {
                _uiState.update { it.copy(editingItem = event.item) }
            }
            is ShoppingEvent.StopEditingItem -> {
                _uiState.update { it.copy(editingItem = null) }
            }
            is ShoppingEvent.SaveEditedItem -> {
                saveEditedItem(event.title, event.quantity, event.categoryId)
            }
        }
    }

    private suspend fun handleReminderUpdate() {
        val items = repository.observeItems().first()
        val anyChecked = items.any { it.isChecked }
        val anyUnchecked = items.any { !it.isChecked }

        if (anyChecked && anyUnchecked) {
            val delayMinutes = settingsRepository.shoppingReminderDelayMinutes.first()
            reminderScheduler.schedule(delayMinutes * 60 * 1000L)
        } else {
            reminderScheduler.cancel()
        }
    }

    private fun addItem() {
        val title = _uiState.value.newItemTitle.trim()
        if (title.isBlank()) return

        val categoryId = _uiState.value.selectedNewItemCategoryId ?: _uiState.value.categories.find { it.isDefault }?.id

        viewModelScope.launch {
            repository.insertItem(
                ShoppingListItem(
                    title = title,
                    categoryId = categoryId,
                    quantity = 1
                )
            )
            _uiState.update { it.copy(newItemTitle = "") }
        }
    }

    private fun reorderCategory(category: ShoppingCategory, up: Boolean) {
        viewModelScope.launch {
            val list = _uiState.value.categories.toMutableList()
            val index = list.indexOfFirst { it.id == category.id }
            if (index == -1) return@launch

            val targetIndex = if (up) index - 1 else index + 1
            if (targetIndex in list.indices) {
                val current = list[index]
                val target = list[targetIndex]

                repository.updateCategory(current.copy(sortOrder = target.sortOrder))
                repository.updateCategory(target.copy(sortOrder = current.sortOrder))
            }
        }
    }

    private fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
            if (_uiState.value.selectedNewItemCategoryId == categoryId) {
                _uiState.update { it.copy(selectedNewItemCategoryId = null) }
            }
        }
    }

    private fun addCategory() {
        val name = _uiState.value.newCategoryName.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            val nextSortOrder = (_uiState.value.categories.maxOfOrNull { it.sortOrder } ?: -1) + 1
            repository.insertCategory(
                ShoppingCategory(
                    name = name,
                    sortOrder = nextSortOrder
                )
            )
            _uiState.update { it.copy(newCategoryName = "", isAddingCategory = false) }
        }
    }

    private fun saveEditedItem(title: String, quantity: Int, categoryId: Long?) {
        val item = _uiState.value.editingItem ?: return
        viewModelScope.launch {
            repository.updateItem(
                item.copy(
                    title = title.trim(),
                    quantity = quantity.coerceAtLeast(1),
                    categoryId = categoryId
                )
            )
            _uiState.update { it.copy(editingItem = null) }
            handleReminderUpdate()
        }
    }
}
