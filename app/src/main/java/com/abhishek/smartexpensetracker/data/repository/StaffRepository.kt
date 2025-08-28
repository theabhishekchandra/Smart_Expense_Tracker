package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IStaffRepository {
    fun getAllStaffFlow(): Flow<List<StaffEntity>>
    fun getActiveStaffFlow(): Flow<List<StaffEntity>>
    suspend fun insertStaff(staff: StaffEntity): Long
    suspend fun updateStaff(staff: StaffEntity)
    suspend fun deleteStaff(staff: StaffEntity)
    suspend fun getStaffById(id: Long): StaffEntity?
    suspend fun getStaffByEmail(email: String): StaffEntity?
    suspend fun searchStaff(query: String): List<StaffEntity>
    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>
    suspend fun countAllStaff(): Int
    suspend fun deactivateStaff(id: Long)
    suspend fun activateStaff(id: Long)
    suspend fun getStaffWithAllocations(staffId: Long): StaffWithAllocations? // or StaffWithAllocationsRoom
}

class StaffRepository @Inject constructor(
    private val dao: StaffDao,
    private val allocationDao: AllocationDao // optional, for joined queries or extra logic
) : IStaffRepository {

    override fun getAllStaffFlow(): Flow<List<StaffEntity>> = dao.getAllStaffFlow()
    override fun getActiveStaffFlow(): Flow<List<StaffEntity>> = dao.getActiveStaffFlow()
    override suspend fun insertStaff(staff: StaffEntity): Long = dao.insertStaff(staff)
    override suspend fun updateStaff(staff: StaffEntity) = dao.updateStaff(staff)
    override suspend fun deleteStaff(staff: StaffEntity) = dao.deleteStaff(staff)
    override suspend fun getStaffById(id: Long): StaffEntity? = dao.getStaffById(id)
    override suspend fun getStaffByEmail(email: String): StaffEntity? = dao.getStaffByEmail(email)
    override suspend fun searchStaff(query: String): List<StaffEntity> = dao.searchStaff(query)
    override suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary> = dao.getStaffWithExpenseSummary()
    override suspend fun countAllStaff(): Int = dao.countAllStaff()
    override suspend fun deactivateStaff(id: Long) = dao.deactivateStaff(id)
    override suspend fun activateStaff(id: Long) = dao.activateStaff(id)

    override suspend fun getStaffWithAllocations(staffId: Long): StaffWithAllocations? {
        // Simple implementation: read staff + allocations and map
        val staff = dao.getStaffById(staffId) ?: return null
        val allocations = allocationDao.getAllocationsForStaff(staffId.toInt())
        return StaffWithAllocations(
            staff = staff,
            allocations = allocations
        )
    }
}
