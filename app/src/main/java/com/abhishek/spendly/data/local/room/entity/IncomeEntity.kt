package com.abhishek.spendly.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incomes")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val incomeId: Long = 0,
    val userId: Long,
    val categoryId: Long?,
    val amount: Double,
    val notes: String?,
    val receiptUri: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)