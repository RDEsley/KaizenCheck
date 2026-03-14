package com.kaizencheck.app.data

import androidx.room.Embedded

data class ChecklistWithCount(
    @Embedded val checklist: ChecklistEntity,
    val completedCount: Int,
    val totalCount: Int
)
