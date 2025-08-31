package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

//2. Expense Table (Already exists – extend with staff + allocation info)
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AllocationEntity::class, parentColumns = ["allocationId"], childColumns = ["allocationId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["categoryId"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("userId"), Index("allocationId"), Index("categoryId")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val expenseId: Long = 0,
    val userId: Long = 0,
    val allocationId: Int? = null, // allocationId means staffId.
    val categoryId: Long? = null,
    val title: String = "",
    val amount: Double = 0.0,
    val notes: String? = null,
    val receiptUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending", // Pending, Approved, Rejected
    val approvedBy: Long? = null,
    val synced: Boolean = false
)

// For ExpenseDao.getMonthlyExpenseTrend()
data class MonthlyExpenseTrend(
    val month: String,   // e.g. "01", "02" etc
    val total: Double
)