package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.dao.ExpenseDao
import com.abhishek.smartexpensetracker.data.local.room.entity.ExpenseEntity
import com.abhishek.smartexpensetracker.data.model.DateFilter
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) : IExpenseRepository {
    override fun getAll(): Flow<List<ExpenseEntity>> = dao.getAll()
    override fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<ExpenseEntity>> = dao.getForDayRange(dayStart, dayEnd)
    override suspend fun insert(expense: ExpenseEntity) = dao.insert(expense)
    override suspend fun update(expense: ExpenseEntity) = dao.update(expense)
    override suspend fun delete(expense: ExpenseEntity) = dao.delete(expense)
    override suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double = dao.totalForDayRange(dayStart, dayEnd) ?: 0.0
    override suspend fun getUnsynced(): List<ExpenseEntity> = dao.getUnsynced()
    override fun getExpensesByFilter(filter: DateFilter): Flow<List<ExpenseEntity>> {
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


//    fun observeById(id: Long): Flow<ExpenseEntity?> = expenseDao.getById(id)
//
//    fun observeAllForUser(userId: Long): Flow<List<ExpenseEntity>> =
//        expenseDao.getAllForUser(userId)
//
//    fun observeForDate(userId: Long, date: Long): Flow<List<ExpenseEntity>> =
//        expenseDao.getForDate(userId, date)
//
//    fun observeMonthlyTrend(userId: Long, from: Long, to: Long): Flow<List<MonthlyExpenseTrend>> =
//        expenseDao.getMonthlyExpenseTrend(userId, from, to)
//
//    fun observeCategorySpending(userId: Long, from: Long, to: Long): Flow<List<CategorySpendingSummary>> =
//        expenseDao.getCategoryWiseSpending(userId, from, to)
//
//    fun observeStaffUsage(from: Long, to: Long): Flow<List<StaffUsageSummary>> =
//        expenseDao.getTotalUsedByStaff(from, to)
//
//    suspend fun sumForPeriod(userId: Long, from: Long, to: Long): Double =
//        expenseDao.getSumForPeriod(userId, from, to)

}

interface IExpenseRepository {
    fun getAll(): Flow<List<ExpenseEntity>>
    fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<ExpenseEntity>>
    suspend fun insert(expense: ExpenseEntity)
    suspend fun update(expense: ExpenseEntity)
    suspend fun delete(expense: ExpenseEntity)
    suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double
    suspend fun getUnsynced(): List<ExpenseEntity>
    fun getExpensesByFilter(filter: DateFilter): Flow<List<ExpenseEntity>>

}