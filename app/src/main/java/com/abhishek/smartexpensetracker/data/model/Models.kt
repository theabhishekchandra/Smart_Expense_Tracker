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
    @PrimaryKey (autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
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
            return ExpenseCategory.entries.find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}

data class DailyTotal(val date: LocalDate, val total: Double)


enum class DateFilter { TODAY, YESTERDAY, LAST_7_DAYS, ALL }
enum class GroupMode { TIME, CATEGORY }

data class ExpenseUiState(
    val expenses: List<ExpenseDM> = emptyList(),
    val searchQuery: String = "",
    val groupMode: GroupMode = GroupMode.TIME,
    val loading: Boolean = false,
    val error: String? = null,
)