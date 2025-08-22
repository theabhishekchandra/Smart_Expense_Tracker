package com.abhishek.smartexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class ExpenseDM(
    val id: Long = 0,
    val title: String? = "",
    val amount: Double? = 0.0,
    val category: String? = "",
    val notes: String? = "",
    val receiptUri: String? = "null",
    val timestamp: Long? = 0L
)