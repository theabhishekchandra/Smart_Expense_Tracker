package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.smartexpensetracker.data.local.room.entity.SyncLogEntity

@Dao
interface SyncLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(syncLog: SyncLogEntity)

    @Query("SELECT * FROM sync_logs WHERE synced = 0 ORDER BY timestamp ASC")
    suspend fun getPendingSync(): List<SyncLogEntity>

    @Query("UPDATE sync_logs SET synced = 1 WHERE syncLogId = :id")
    suspend fun markAsSynced(id: Long)

    @Query("UPDATE sync_logs SET retryCount = retryCount + 1 WHERE syncLogId = :id")
    suspend fun incrementRetry(id: Long)
}
