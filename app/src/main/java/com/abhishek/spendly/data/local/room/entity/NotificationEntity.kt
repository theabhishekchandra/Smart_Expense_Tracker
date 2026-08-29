package com.abhishek.spendly.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 6. Notification Table (alerts, approvals, budget warnings)
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val notificationId: Long = 0,
    val userId: Long,
    val type: String, // budget_warning / expense_status / allocation_assigned
    val message: String,
    val relatedEntity: String? = null, // e.g., "expense", "budget"
    val relatedId: Long? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)