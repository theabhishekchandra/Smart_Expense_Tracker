package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AllocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllocation(allocation: AllocationEntity)

    @Query("SELECT * FROM allocations WHERE staffId = :staffId")
    suspend fun getAllocationsForStaff(staffId: Int): List<AllocationEntity>

    @Query(
        """
    SELECT a.id, a.staffId, a.title, a.category, a.allocatedAmount, a.notes, a.createdAt, a.expiresAt, a.status,
           (SELECT IFNULL(SUM(e.amount),0) FROM expenses e WHERE e.allocationId = a.id) as usedAmount,
           ROUND(((SELECT IFNULL(SUM(e.amount),0) FROM expenses e WHERE e.allocationId = a.id) * 100.0 / a.allocatedAmount), 2) as utilizationPercent
    FROM allocations a
    WHERE a.staffId = :staffId
"""
    )
    suspend fun getAllocationUsageForStaff(staffId: Int): List<AllocationUsageSummary>
}
