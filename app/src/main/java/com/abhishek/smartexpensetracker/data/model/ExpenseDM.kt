package com.abhishek.smartexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import android.net.Uri

// Expense status for business workflow
enum class ExpenseStatus {
    PENDING,
    APPROVED,
    REJECTED
}

// User roles
enum class UserRole {
    PERSONAL,
    ADMIN,
    APPROVER,
    ENTRY_ONLY,
    VIEWER
}

data class ExpenseDM(
    val id: String = UUID.randomUUID().toString(), // unique ID
    val userId: String = "",                             // who added the expense
    val userName: String? = null,                   // name of the user (useful for Admin view)
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",                           // can use ExpenseCategory.name
    val notes: String? = null,
    val receiptUri: Uri? = null,                    // optional receipt image
    val timestamp: Long = System.currentTimeMillis(),
    val status: ExpenseStatus = ExpenseStatus.PENDING // for business workflow
)
