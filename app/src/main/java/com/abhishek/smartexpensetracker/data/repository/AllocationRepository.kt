package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.dao.AllocationDao
import com.abhishek.smartexpensetracker.data.local.room.entity.AllocationEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.AllocationUsageSummary
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

//    suspend fun upsert(allocation: AllocationEntity): Long = allocationDao.insert(allocation)
//    suspend fun update(allocation: AllocationEntity) = allocationDao.update(allocation)
//    suspend fun delete(allocation: AllocationEntity) = allocationDao.delete(allocation)
//
//    fun observeForStaff(staffId: Int): Flow<List<AllocationEntity>> =
//        allocationDao.getAllocationsForStaff(staffId)
//
//    fun observeStaffWithAllocations(staffId: Int): Flow<StaffWithAllocations> =
//        allocationDao.getStaffWithAllocations(staffId)
//
//    fun observeUsageForStaff(staffId: Int): Flow<List<AllocationUsageSummary>> =
//        allocationDao.getAllocationUsageForStaff(staffId)
}
