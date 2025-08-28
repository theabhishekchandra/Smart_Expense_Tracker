package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StaffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(staff: List<StaffEntity>)

    @Update
    suspend fun updateStaff(staff: StaffEntity)

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)

    @Query("SELECT * FROM staff ORDER BY name ASC")
    fun getAllStaffFlow(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveStaffFlow(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE id = :id LIMIT 1")
    suspend fun getStaffById(id: Long): StaffEntity?

    @Query("SELECT * FROM staff WHERE email = :email LIMIT 1")
    suspend fun getStaffByEmail(email: String): StaffEntity?

    @Query("SELECT * FROM staff WHERE role = :role ORDER BY name ASC")
    suspend fun getStaffByRole(role: String): List<StaffEntity>

    @Query("""
        SELECT * FROM staff
        WHERE (name LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR employeeId LIKE '%' || :query || '%')
        ORDER BY name ASC
    """)
    suspend fun searchStaff(query: String): List<StaffEntity>

    @Query("UPDATE staff SET isActive = 0 WHERE id = :id")
    suspend fun deactivateStaff(id: Long)

    @Query("UPDATE staff SET isActive = 1 WHERE id = :id")
    suspend fun activateStaff(id: Long)

    @Query("SELECT COUNT(*) FROM staff")
    suspend fun countAllStaff(): Int

    // Example join: staff with expense totals (uses expenses table)
    @Query("""
        SELECT s.id as id, s.name as name, s.email as email, s.role as role, s.phone as phone, s.profilePicUri as profilePicUri, s.createdAt as createdAt, s.isActive as isActive,
               IFNULL(SUM(e.amount), 0) as totalExpenses
        FROM staff s
        LEFT JOIN expenses e ON e.userId = s.id
        GROUP BY s.id
        ORDER BY totalExpenses DESC
    """)
    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>

    // Get staff with allocations (if allocations table references staffId)
    @Transaction
    @Query("""
        SELECT s.* FROM staff s
        WHERE s.id = :staffId
        LIMIT 1
    """)
    suspend fun getStaffWithAllocations(staffId: Long): StaffWithAllocations?
}
