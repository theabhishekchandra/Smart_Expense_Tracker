package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * StaffEntity - stores staff / user information used in business mode.
 * Includes many useful columns for business workflows (approval, contact, meta).
 */
@Entity(
    tableName = "staff",
    indices = [Index(value = ["email"], unique = true), Index(value = ["employeeId"], unique = true)]
)
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val staffId: Int = 0,
    val employeeId: String,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String,
    val department: String?,
    val designation: String?,
    val managerId: Int?,
    val joiningDate: String?,
    val salary: Double?,
    val profilePicUri: String?,
    val permissionsJson: String?,
    val isActive: Boolean = true,
    val lastLoginAt: String?,
    val notes: String?,
    val createdAt: Long = System.currentTimeMillis()
)


data class StaffWithAllocations(
    @Embedded val staff: StaffEntity,
    @Relation(
        parentColumn = "staffId",
        entityColumn = "staffId"
    )
    val allocations: List<AllocationEntity>
)


// For the getStaffWithExpenseSummary() result
data class StaffExpenseSummary(
    @ColumnInfo ("staffId") val id: Long,
    val name: String,
    val email: String?,
    val role: String?,
    val phone: String?,
    val profilePicUri: String?,
    val createdAt: Long,
    val isActive: Boolean,
    val totalExpenses: Double
)

// For ExpenseDao.getTotalUsedByStaff()
data class StaffUsageSummary(
    val staffName: String,
    val totalUsed: Double
)