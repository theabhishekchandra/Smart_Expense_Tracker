package com.abhishek.spendly.data.repository

import com.abhishek.spendly.data.local.room.dao.BudgetDao
import com.abhishek.spendly.data.local.room.entity.BudgetEntity
import com.abhishek.spendly.data.local.room.entity.BudgetWithUtilization
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

//    suspend fun upsert(budget: BudgetEntity): Long = budgetDao.insert(budget)
//    suspend fun update(budget: BudgetEntity) = budgetDao.update(budget)
//    suspend fun delete(budget: BudgetEntity) = budgetDao.delete(budget)
//
//    fun observeById(id: Long) = budgetDao.getById(id)
//    fun observeUtilization(): Flow<List<BudgetWithUtilization>> = budgetDao.getBudgetUtilization()
}
