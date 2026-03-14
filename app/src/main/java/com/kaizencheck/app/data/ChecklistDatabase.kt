package com.kaizencheck.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ChecklistEntity::class, ChecklistItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChecklistDatabase : RoomDatabase() {

    abstract fun checklistDao(): ChecklistDao

    companion object {
        @Volatile
        private var INSTANCE: ChecklistDatabase? = null

        fun getDatabase(context: Context): ChecklistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecklistDatabase::class.java,
                    "kaizen_check_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
