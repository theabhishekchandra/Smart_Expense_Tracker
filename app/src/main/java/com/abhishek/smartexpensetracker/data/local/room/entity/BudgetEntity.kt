package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 4. Budget Table (Admin sets overall company/department budgets)
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val budgetId: Long = 0,
    val name: String = "Default Budget",
    val periodType: String,
    val periodStart: Long,
    val periodEnd: Long,
    val totalLimit: Double,
    val usedAmount: Double = 0.0,
    val createdBy: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "Active"
)


// For BudgetDao.getBudgetUtilization()
data class BudgetWithUtilization(
    @ColumnInfo ("budgetId")val id: Long,
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