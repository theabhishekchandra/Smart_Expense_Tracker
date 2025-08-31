package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 5. Category Table (Custom categories + AI classification support)
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    val parentId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isSystem: Boolean = false
)
// For ExpenseDao.getCategoryWiseSpending()
data class CategorySpendingSummary(
    val categoryName: String?,
    val total: Double
)