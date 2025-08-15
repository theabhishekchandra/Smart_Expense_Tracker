package com.abhishek.smartexpensetracker.utils

import android.util.Log
import com.abhishek.smartexpensetracker.data.repository.IExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SyncManager(private val repo: IExpenseRepository) {
    suspend fun syncOnce() = withContext(Dispatchers.IO) {
        try {
            val unsynced = repo.getUnsynced()
            if (unsynced.isEmpty()) return@withContext
            delay(1200)
            unsynced.forEach { e ->
                val updated = e.copy(synced = true)
                repo.update(updated)
            }
            Log.d("SyncManager", "Synced ${unsynced.size} items")
        } catch (ex: Exception) {
            Log.w("SyncManager", "Sync failed: ${ex.message}")
        }
    }
}
