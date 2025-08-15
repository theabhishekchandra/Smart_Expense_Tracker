package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String?,
    val receiptPath: String?,
    val timestamp: Long
)