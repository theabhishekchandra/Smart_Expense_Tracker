package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//3. Allocation Table (Admin assigned budgets to staff)
@Entity(tableName = "allocations")
data class AllocationEntity(
    @PrimaryKey(autoGenerate = true) val allocationId: Int = 0,
    val staffId: Int,
    val title: String,
    val category: String,
    val allocatedAmount: Double,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val status: String = "Active"
)

// For AllocationDao.getAllocationUsageForStaff()
data class AllocationUsageSummary(
    @ColumnInfo(name = "allocationId") val id: Long,
    val staffId: Long,
    val title: String,
    val category: String,
    val allocatedAmount: Double,
    val usedAmount: Double,
    val utilizationPercent: Double,
    val notes: String?,
    val createdAt: Long,
    val expiresAt: Long?,
    val status: String
)