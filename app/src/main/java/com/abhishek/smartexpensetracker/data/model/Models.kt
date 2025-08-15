package com.abhishek.smartexpensetracker.data.model

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

enum class Category { Staff, Travel, Food, Utility }

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String? = null,
    val receiptUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)


/**
 * Enum class for supported categories
 */
enum class ExpenseCategory(val displayName: String) {
    STAFF("Staff"),
    TRAVEL("Travel"),
    FOOD("Food"),
    UTILITY("Utility");

    companion object {
        fun fromDisplayName(name: String): ExpenseCategory? {
            return values().find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}

data class DailyTotal(val date: LocalDate, val total: Double)