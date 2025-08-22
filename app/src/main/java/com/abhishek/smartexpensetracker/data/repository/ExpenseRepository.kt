package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.ExpenseDao
import com.abhishek.smartexpensetracker.data.model.DateFilter
import com.abhishek.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) : IExpenseRepository {
    override fun getAll(): Flow<List<Expense>> = dao.getAll()
    override fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<Expense>> = dao.getForDayRange(dayStart, dayEnd)
    override suspend fun insert(expense: Expense) = dao.insert(expense)
    override suspend fun update(expense: Expense) = dao.update(expense)
    override suspend fun delete(expense: Expense) = dao.delete(expense)
    override suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double = dao.totalForDayRange(dayStart, dayEnd) ?: 0.0
    override suspend fun getUnsynced(): List<Expense> = dao.getUnsynced()


    override fun getExpensesByFilter(filter: DateFilter): Flow<List<Expense>> {
        val cal = Calendar.getInstance()

        return when (filter) {
            DateFilter.TODAY -> {
                val start = cal.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                val end = start + 24 * 60 * 60 * 1000
                dao.getForDayRange(start, end)
            }

            DateFilter.YESTERDAY -> {
                val end = cal.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                val start = end - 24 * 60 * 60 * 1000
                dao.getForDayRange(start, end)
            }

            DateFilter.LAST_7_DAYS -> {
                val end = System.currentTimeMillis()
                val start = end - (7 * 24 * 60 * 60 * 1000)
                dao.getForDayRange(start, end)
            }

            DateFilter.ALL -> {
                dao.getAll()
            }
        }
    }
}
