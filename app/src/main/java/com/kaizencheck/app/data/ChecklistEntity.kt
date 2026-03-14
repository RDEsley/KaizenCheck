package com.kaizencheck.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklists")
data class ChecklistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String = "checklist",
    val color: Long = 0xFF00796B,
    val createdAt: Long = System.currentTimeMillis()
)
