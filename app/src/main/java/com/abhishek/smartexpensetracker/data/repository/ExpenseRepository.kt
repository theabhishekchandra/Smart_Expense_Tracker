package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.ExpenseDao
import com.abhishek.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository private constructor(private val dao: ExpenseDao) : IExpenseRepository {
    override fun getAll(): Flow<List<Expense>> = dao.getAll()
    override fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<Expense>> = dao.getForDayRange(dayStart, dayEnd)
    override suspend fun insert(expense: Expense) = dao.insert(expense)
    override suspend fun update(expense: Expense) = dao.update(expense)
    override suspend fun delete(expense: Expense) = dao.delete(expense)
    override suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double = dao.totalForDayRange(dayStart, dayEnd) ?: 0.0
    override suspend fun getUnsynced(): List<Expense> = dao.getUnsynced()

    companion object {
        @Volatile private var instance: ExpenseRepository? = null
        fun getInstance(dao: ExpenseDao): ExpenseRepository {
            return instance ?: synchronized(this) {
                val inst = ExpenseRepository(dao)
                instance = inst
                inst
            }
        }
    }
}
