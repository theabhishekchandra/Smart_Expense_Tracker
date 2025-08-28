package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.BudgetDao
import com.abhishek.smartexpensetracker.data.local.room.BudgetEntity
import com.abhishek.smartexpensetracker.data.local.room.BudgetWithUtilization
import javax.inject.Inject

interface IBudgetRepository {
    suspend fun insertBudget(budget: BudgetEntity)
    suspend fun getBudgetsForUser(userId: Long): List<BudgetEntity>
    suspend fun getBudgetUtilization(userId: Long): List<BudgetWithUtilization>
    suspend fun archiveBudget(budgetId: Long)
}

class BudgetRepository @Inject constructor(
    private val dao: BudgetDao
) : IBudgetRepository {
    override suspend fun insertBudget(budget: BudgetEntity) = dao.insertBudget(budget)
    override suspend fun getBudgetsForUser(userId: Long): List<BudgetEntity> = dao.getBudgetsForUser(userId)
    override suspend fun getBudgetUtilization(userId: Long): List<BudgetWithUtilization> = dao.getBudgetUtilization(userId)
    override suspend fun archiveBudget(budgetId: Long) = dao.archiveBudget(budgetId)
}
