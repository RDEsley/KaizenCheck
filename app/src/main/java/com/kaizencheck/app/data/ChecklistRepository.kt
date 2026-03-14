package com.kaizencheck.app.data

import kotlinx.coroutines.flow.Flow

class ChecklistRepository(private val dao: ChecklistDao) {

    val allChecklists: Flow<List<ChecklistEntity>> = dao.getAllChecklists()

    val allChecklistsWithCounts: Flow<List<ChecklistWithCount>> = dao.getAllChecklistsWithCounts()

    fun getChecklist(id: Long) = dao.getChecklistById(id)

    fun getItems(checklistId: Long): Flow<List<ChecklistItemEntity>> =
        dao.getItemsByChecklistId(checklistId)

    fun getCompletedCount(checklistId: Long): Flow<Int> =
        dao.getCompletedCount(checklistId)

    fun getTotalCount(checklistId: Long): Flow<Int> =
        dao.getTotalCount(checklistId)

    suspend fun insertChecklist(name: String): Long {
        return dao.insertChecklist(ChecklistEntity(name = name))
    }

    suspend fun updateChecklist(checklist: ChecklistEntity) {
        dao.updateChecklist(checklist)
    }

    suspend fun deleteChecklist(id: Long) {
        dao.deleteChecklist(id)
    }

    suspend fun insertItem(checklistId: Long, title: String, position: Int = 0): Long {
        return dao.insertItem(
            ChecklistItemEntity(
                checklistId = checklistId,
                title = title,
                position = position
            )
        )
    }

    suspend fun updateItem(item: ChecklistItemEntity) {
        dao.updateItem(item)
    }

    suspend fun toggleItemCompleted(item: ChecklistItemEntity) {
        dao.setItemCompleted(item.id, !item.isCompleted)
    }

    suspend fun deleteItem(itemId: Long) {
        dao.deleteItem(itemId)
    }
}
