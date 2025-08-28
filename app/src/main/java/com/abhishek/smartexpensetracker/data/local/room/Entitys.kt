package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// 1. User Table (for Admin & Staff login)
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String, // Or Firebase/Auth provider ID
    val role: String, // "admin" / "staff" / "viewer" / "approver"
    val phone: String? = null,
    val profilePicUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)


//2. Expense Table (Already exists – extend with staff + allocation info)
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AllocationEntity::class, parentColumns = ["id"], childColumns = ["allocationId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("userId"), Index("allocationId"), Index("categoryId")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long = 0,
    val allocationId: Long? = null,
    val categoryId: Long? = null, // normalized link to Category table
    val title: String = "",
    val amount: Double = 0.0,
    val notes: String? = null,
    val receiptUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending", // Pending, Approved, Rejected
    val approvedBy: Long? = null, // Admin/Approver userId
    val synced: Boolean = false
)


//3. Allocation Table (Admin assigned budgets to staff)
@Entity(
    tableName = "allocations",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["staffId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("staffId")]
)
data class AllocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val staffId: Long,
    val title: String,
    val category: String,
    val allocatedAmount: Double,
    val usedAmount: Double = 0.0,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val status: String = "Active" // Active / Closed / Expired
)


// 4. Budget Table (Admin sets overall company/department budgets)
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "Default Budget",
    val periodType: String, // monthly / weekly / yearly
    val periodStart: Long,
    val periodEnd: Long,
    val totalLimit: Double,
    val usedAmount: Double = 0.0,
    val createdBy: Long? = null, // Admin userId
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "Active" // Active / Archived
)


// 5. Category Table (Custom categories + AI classification support)
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    val parentId: Long? = null, // Subcategories
    val createdAt: Long = System.currentTimeMillis(),
    val isSystem: Boolean = false // true = built-in, false = user-added
)


// 6. Notification Table (alerts, approvals, budget warnings)
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: String, // budget_warning / expense_status / allocation_assigned
    val message: String,
    val relatedEntity: String? = null, // e.g., "expense", "budget"
    val relatedId: Long? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

// 7. SyncLog Table (track offline → online sync).
@Entity(tableName = "sync_logs")
data class SyncLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String, // expense / allocation / user / budget
    val entityId: Long,
    val action: String, // insert / update / delete
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false,
    val retryCount: Int = 0
)


// For AllocationDao.getAllocationUsageForStaff()
data class AllocationUsageSummary(
    val id: Long,
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

// For BudgetDao.getBudgetUtilization()
data class BudgetWithUtilization(
    val id: Long,
    val name: String,
    val periodType: String,
    val periodStart: Long,
    val periodEnd: Long,
    val totalLimit: Double,
    val usedAmount: Double,
    val utilizationPercent: Double,
    val createdBy: Long?,
    val createdAt: Long,
    val status: String
)

// For ExpenseDao.getTotalUsedByStaff()
data class StaffUsageSummary(
    val staffName: String,
    val totalUsed: Double
)

// For ExpenseDao.getMonthlyExpenseTrend()
data class MonthlyExpenseTrend(
    val month: String,   // e.g. "01", "02" etc
    val total: Double
)

// For ExpenseDao.getCategoryWiseSpending()
data class CategorySpendingSummary(
    val categoryName: String?,
    val total: Double
)

/**
 * StaffEntity - stores staff / user information used in business mode.
 * Includes many useful columns for business workflows (approval, contact, meta).
 */
@Entity(
    tableName = "staff",
    indices = [Index(value = ["email"], unique = true), Index(value = ["employeeId"], unique = true)]
)
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val employeeId: String? = null,         // Company employee id / ref
    val name: String,
    val email: String,
    val phone: String? = null,
    val role: String = "entry",              // "admin" | "approver" | "entry" | "viewer"
    val department: String? = null,
    val designation: String? = null,
    val managerId: Long? = null,            // reportsTo
    val joiningDate: Long? = null,          // epoch millis
    val salary: Double? = null,
    val profilePicUri: String? = null,
    val permissionsJson: String? = null,    // optional advanced permissions serialized
    val isActive: Boolean = true,
    val lastLoginAt: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

// For the getStaffWithExpenseSummary() result
data class StaffExpenseSummary(
    val id: Long,
    val name: String,
    val email: String?,
    val role: String?,
    val phone: String?,
    val profilePicUri: String?,
    val createdAt: Long,
    val isActive: Boolean,
    val totalExpenses: Double
)

// For a transaction that returns staff + allocations
data class StaffWithAllocations(
    val staff: StaffEntity,
    val allocations: List<AllocationEntity>
)