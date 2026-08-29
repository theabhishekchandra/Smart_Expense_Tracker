package com.abhishek.spendly.data.repository

import com.abhishek.spendly.data.local.room.dao.SyncLogDao
import com.abhishek.spendly.data.local.room.entity.SyncLogEntity
import javax.inject.Inject

interface ISyncLogRepository {
    suspend fun insertLog(syncLog: SyncLogEntity)
    suspend fun getPendingSync(): List<SyncLogEntity>
    suspend fun markAsSynced(id: Long)
    suspend fun incrementRetry(id: Long)
}

class SyncLogRepository @Inject constructor(
    private val dao: SyncLogDao
) : ISyncLogRepository {
    override suspend fun insertLog(syncLog: SyncLogEntity) = dao.insertLog(syncLog)
    override suspend fun getPendingSync(): List<SyncLogEntity> = dao.getPendingSync()
//    fun observePending(): Flow<List<SyncLog>> = syncLogDao.getPendingSyncs()
    override suspend fun markAsSynced(id: Long) = dao.markAsSynced(id)
    override suspend fun incrementRetry(id: Long) = dao.incrementRetry(id)
}
