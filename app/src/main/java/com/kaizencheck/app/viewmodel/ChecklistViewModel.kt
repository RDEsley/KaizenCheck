package com.kaizencheck.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kaizencheck.app.data.ChecklistDatabase
import com.kaizencheck.app.data.ChecklistEntity
import com.kaizencheck.app.data.ChecklistItemEntity
import com.kaizencheck.app.data.ChecklistRepository
import com.kaizencheck.app.data.ChecklistWithCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {

    val checklistsWithCounts: StateFlow<List<ChecklistWithCount>> = repository.allChecklistsWithCounts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getItemsFlow(checklistId: Long): Flow<List<ChecklistItemEntity>> =
        repository.getItems(checklistId)

    suspend fun getChecklistName(id: Long): String? =
        repository.getChecklist(id)?.name

    fun addChecklist(name: String) {
        viewModelScope.launch {
            repository.insertChecklist(name)
        }
    }

    fun updateChecklist(checklist: ChecklistEntity) {
        viewModelScope.launch {
            repository.updateChecklist(checklist)
        }
    }

    fun deleteChecklist(id: Long) {
        viewModelScope.launch {
            repository.deleteChecklist(id)
        }
    }

    fun addItem(checklistId: Long, title: String) {
        viewModelScope.launch {
            repository.insertItem(checklistId, title)
        }
    }

    fun toggleItem(item: ChecklistItemEntity) {
        viewModelScope.launch {
            repository.toggleItemCompleted(item)
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
        }
    }

    companion object {
        fun factory(database: ChecklistDatabase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ChecklistViewModel(
                        ChecklistRepository(database.checklistDao())
                    ) as T
                }
            }
    }
}
