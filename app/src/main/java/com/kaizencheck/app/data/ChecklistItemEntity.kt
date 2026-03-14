package com.kaizencheck.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = ChecklistEntity::class,
            parentColumns = ["id"],
            childColumns = ["checklistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("checklistId")]
)
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val checklistId: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val position: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
