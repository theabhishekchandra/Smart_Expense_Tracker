package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
    fun getAll(): Flow<List<Expense>>
    fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<Expense>>
    suspend fun insert(expense: Expense)
    suspend fun update(expense: Expense)
    suspend fun delete(expense: Expense)
    suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double
    suspend fun getUnsynced(): List<Expense>
}
