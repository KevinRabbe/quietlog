package com.kevinrabbe.quietlog.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinrabbe.quietlog.domain.model.ShoppingListItem
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
    }

    private fun observeItems() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.observeItems().collect { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }

    fun onEvent(event: ShoppingEvent) {
        when (event) {
            is ShoppingEvent.TitleChanged -> {
                _uiState.update { it.copy(newItemTitle = event.title) }
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
        val title = _uiState.value.newItemTitle
        if (title.isBlank()) return

        viewModelScope.launch {
            repository.insertItem(ShoppingListItem(title = title))
            _uiState.update { it.copy(newItemTitle = "") }
        }
    }
}
