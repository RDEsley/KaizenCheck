package com.kaizencheck.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklists ORDER BY createdAt DESC")
    fun getAllChecklists(): Flow<List<ChecklistEntity>>

    @Query("""
        SELECT c.id, c.name, c.icon, c.color, c.createdAt,
        (SELECT COUNT(*) FROM checklist_items WHERE checklistId = c.id AND isCompleted = 1) as completedCount,
        (SELECT COUNT(*) FROM checklist_items WHERE checklistId = c.id) as totalCount
        FROM checklists c
        ORDER BY c.createdAt DESC
    """)
    fun getAllChecklistsWithCounts(): Flow<List<ChecklistWithCount>>

    @Query("SELECT * FROM checklists WHERE id = :id")
    suspend fun getChecklistById(id: Long): ChecklistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(checklist: ChecklistEntity): Long

    @Update
    suspend fun updateChecklist(checklist: ChecklistEntity)

    @Query("DELETE FROM checklists WHERE id = :id")
    suspend fun deleteChecklist(id: Long)

    @Query("SELECT * FROM checklist_items WHERE checklistId = :checklistId ORDER BY position ASC, createdAt ASC")
    fun getItemsByChecklistId(checklistId: Long): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItemEntity): Long

    @Update
    suspend fun updateItem(item: ChecklistItemEntity)

    @Query("UPDATE checklist_items SET isCompleted = :completed WHERE id = :itemId")
    suspend fun setItemCompleted(itemId: Long, completed: Boolean)

    @Query("DELETE FROM checklist_items WHERE id = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("SELECT COUNT(*) FROM checklist_items WHERE checklistId = :checklistId AND isCompleted = 1")
    fun getCompletedCount(checklistId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM checklist_items WHERE checklistId = :checklistId")
    fun getTotalCount(checklistId: Long): Flow<Int>
}
