package com.abhishek.spendly.ui.screens.lender

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private fun todayFormatted(): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

data class LenderRecord(
    val id: String,
    val name: String,
    val mobile: String,
    val amount: Double,
    val isGiven: Boolean,
    val dueDate: String,
    val notes: String,
    val status: LoanStatus = LoanStatus.PENDING,
    val createdAt: String = todayFormatted()
)

fun LenderRecord.toListItem() = LenderBorrowerDM(id = id, name = name, amount = amount, status = status)

fun LenderRecord.toPersonDetail() = PersonDetail(
    name = name,
    totalAmount = amount,
    status = status.name.lowercase().replaceFirstChar { it.uppercase() },
    lastUpdated = createdAt,
    transactions = listOf(
        Transaction(
            date = createdAt,
            amount = amount,
            status = status.name.lowercase().replaceFirstChar { it.uppercase() },
            notes = notes.ifBlank { if (isGiven) "Money given" else "Money taken" }
        )
    )
)

/** In-memory source of truth for lender/borrower records, shared by every [LenderViewModel]
 * instance so add/edit/delete made on one screen is reflected on the others. */
@Singleton
class LenderRepository @Inject constructor() {
    private val _records = MutableStateFlow(sampleRecords())
    val records: StateFlow<List<LenderRecord>> = _records.asStateFlow()

    fun add(record: LenderRecord) {
        _records.update { it + record }
    }

    fun update(record: LenderRecord) {
        _records.update { list -> list.map { if (it.id == record.id) record else it } }
    }

    fun delete(id: String) {
        _records.update { list -> list.filterNot { it.id == id } }
    }

    fun markAsPaid(id: String) {
        _records.update { list -> list.map { if (it.id == id) it.copy(status = LoanStatus.PAID) else it } }
    }

    fun getById(id: String): LenderRecord? = _records.value.find { it.id == id }

    private fun sampleRecords(): List<LenderRecord> = listOf(
        LenderRecord("1", "Ravi Sharma", "9876543210", 5000.0, isGiven = true, dueDate = "10 Sep 2025", notes = "Personal loan for bike purchase", status = LoanStatus.PENDING),
        LenderRecord("2", "Anjali Gupta", "9123456780", 15000.0, isGiven = true, dueDate = "20 Aug 2025", notes = "Wedding expenses", status = LoanStatus.PAID),
        LenderRecord("3", "Mohit Verma", "9898989898", 8000.0, isGiven = true, dueDate = "30 Sep 2025", notes = "Business investment", status = LoanStatus.PENDING),
        LenderRecord("4", "Amit Kumar", "7001234567", 2000.0, isGiven = false, dueDate = "28 Aug 2025", notes = "Medical emergency", status = LoanStatus.OVERDUE)
    )
}

@HiltViewModel
class LenderViewModel @Inject constructor(
    private val repository: LenderRepository
) : ViewModel() {
    val records: StateFlow<List<LenderRecord>> = repository.records

    fun addRecord(name: String, mobile: String, amount: String, isGiven: Boolean, dueDate: String, notes: String) {
        val parsedAmount = amount.toDoubleOrNull() ?: return
        if (name.isBlank()) return
        repository.add(
            LenderRecord(
                id = UUID.randomUUID().toString(),
                name = name,
                mobile = mobile,
                amount = parsedAmount,
                isGiven = isGiven,
                dueDate = dueDate,
                notes = notes
            )
        )
    }

    fun updateRecord(id: String, name: String, mobile: String, amount: String, isGiven: Boolean, dueDate: String, notes: String) {
        val existing = repository.getById(id) ?: return
        if (name.isBlank()) return
        val parsedAmount = amount.toDoubleOrNull() ?: existing.amount
        repository.update(
            existing.copy(name = name, mobile = mobile, amount = parsedAmount, isGiven = isGiven, dueDate = dueDate, notes = notes)
        )
    }

    fun deleteRecord(id: String) = repository.delete(id)
    fun markAsPaid(id: String) = repository.markAsPaid(id)
    fun getById(id: String): LenderRecord? = repository.getById(id)
}
