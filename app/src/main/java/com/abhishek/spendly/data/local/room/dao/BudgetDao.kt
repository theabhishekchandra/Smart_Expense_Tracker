package com.abhishek.spendly.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.spendly.data.local.room.entity.BudgetEntity
import com.abhishek.spendly.data.local.room.entity.BudgetWithUtilization

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets WHERE createdBy = :userId")
    suspend fun getBudgetsForUser(userId: Long): List<BudgetEntity>

    @Query("""
        SELECT b.*, 
        (SELECT IFNULL(SUM(e.amount),0) FROM expenses e WHERE e.timestamp BETWEEN b.periodStart AND b.periodEnd) as usedAmount,
        ROUND(((SELECT IFNULL(SUM(e.amount),0) FROM expenses e WHERE e.timestamp BETWEEN b.periodStart AND b.periodEnd) * 100.0 / b.totalLimit), 2) as utilizationPercent
        FROM budgets b
        WHERE b.createdBy = :userId
    """)
    suspend fun getBudgetUtilization(userId: Long): List<BudgetWithUtilization>

    @Query("UPDATE budgets SET status = 'Archived' WHERE budgetId = :budgetId")
    suspend fun archiveBudget(budgetId: Long)
}
