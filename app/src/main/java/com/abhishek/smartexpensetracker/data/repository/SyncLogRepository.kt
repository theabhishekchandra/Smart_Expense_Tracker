package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.SyncLog
import com.abhishek.smartexpensetracker.data.local.room.SyncLogDao
import javax.inject.Inject

interface ISyncLogRepository {
    suspend fun insertLog(syncLog: SyncLog)
    suspend fun getPendingSync(): List<SyncLog>
    suspend fun markAsSynced(id: Long)
    suspend fun incrementRetry(id: Long)
}

class SyncLogRepository @Inject constructor(
    private val dao: SyncLogDao
) : ISyncLogRepository {
    override suspend fun insertLog(syncLog: SyncLog) = dao.insertLog(syncLog)
    override suspend fun getPendingSync(): List<SyncLog> = dao.getPendingSync()
    override suspend fun markAsSynced(id: Long) = dao.markAsSynced(id)
    override suspend fun incrementRetry(id: Long) = dao.incrementRetry(id)
}
