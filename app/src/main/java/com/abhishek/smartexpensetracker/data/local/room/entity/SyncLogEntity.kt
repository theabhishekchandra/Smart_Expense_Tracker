package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 7. SyncLog Table (track offline → online sync).
@Entity(tableName = "sync_logs")
data class SyncLogEntity(
    @PrimaryKey(autoGenerate = true) val syncLogId: Long = 0,
    val entityType: String, // expense / allocation / user / budget
    val entityId: Long,
    val action: String, // insert / update / delete
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false,
    val retryCount: Int = 0
)