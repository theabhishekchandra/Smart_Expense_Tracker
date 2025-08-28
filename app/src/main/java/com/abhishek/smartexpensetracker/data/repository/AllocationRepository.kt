package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.AllocationDao
import com.abhishek.smartexpensetracker.data.local.room.AllocationEntity
import com.abhishek.smartexpensetracker.data.local.room.AllocationUsageSummary
import javax.inject.Inject

interface IAllocationRepository {
    suspend fun insertAllocation(allocation: AllocationEntity)
    suspend fun getAllocationsForStaff(staffId: Int): List<AllocationEntity>
    suspend fun getAllocationUsageForStaff(staffId: Int): List<AllocationUsageSummary>
}

class AllocationRepository @Inject constructor(
    private val dao: AllocationDao
) : IAllocationRepository {
    override suspend fun insertAllocation(allocation: AllocationEntity) = dao.insertAllocation(allocation)
    override suspend fun getAllocationsForStaff(staffId: Int): List<AllocationEntity> = dao.getAllocationsForStaff(staffId)
    override suspend fun getAllocationUsageForStaff(staffId: Int): List<AllocationUsageSummary> = dao.getAllocationUsageForStaff(staffId)
}
