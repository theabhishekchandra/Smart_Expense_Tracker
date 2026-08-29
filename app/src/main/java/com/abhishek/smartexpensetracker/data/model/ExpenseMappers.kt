package com.abhishek.smartexpensetracker.data.model

import android.net.Uri
import com.abhishek.smartexpensetracker.data.local.room.entity.ExpenseEntity

const val DEFAULT_LOCAL_USER_ID: Long = 1L

fun ExpenseEntity.toDomain(categoryNameById: Map<Long, String>): ExpenseDM = ExpenseDM(
    id = expenseId,
    userId = userId.toString(),
    title = title,
    amount = amount,
    category = categoryId?.let { categoryNameById[it] }.orEmpty(),
    notes = notes,
    receiptUri = receiptUri?.let { runCatching { Uri.parse(it) }.getOrNull() },
    timestamp = timestamp,
    status = when (status) {
        "Approved" -> ExpenseStatus.APPROVED
        "Rejected" -> ExpenseStatus.REJECTED
        else -> ExpenseStatus.PENDING
    }
)

fun ExpenseDM.toEntity(categoryId: Long?): ExpenseEntity = ExpenseEntity(
    expenseId = id,
    userId = userId.toLongOrNull() ?: DEFAULT_LOCAL_USER_ID,
    categoryId = categoryId,
    title = title,
    amount = amount,
    notes = notes,
    receiptUri = receiptUri?.toString(),
    timestamp = timestamp,
    status = when (status) {
        ExpenseStatus.APPROVED -> "Approved"
        ExpenseStatus.REJECTED -> "Rejected"
        ExpenseStatus.PENDING -> "Pending"
    }
)
