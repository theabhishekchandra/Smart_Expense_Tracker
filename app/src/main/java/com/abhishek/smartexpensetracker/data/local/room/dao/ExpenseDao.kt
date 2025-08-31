package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.*
import com.abhishek.smartexpensetracker.data.local.room.entity.CategorySpendingSummary
import com.abhishek.smartexpensetracker.data.local.room.entity.ExpenseEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.MonthlyExpenseTrend
import com.abhishek.smartexpensetracker.data.local.room.entity.StaffUsageSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    /** Expense Show -> How much amount is spending by each staff or personal use.*/
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE timestamp >= :dayStart AND timestamp < :dayEnd ORDER BY timestamp DESC")
    fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expenseEntity: ExpenseEntity)

    @Update
    suspend fun update(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun delete(expenseEntity: ExpenseEntity)

    @Query("SELECT SUM(amount) FROM expenses WHERE timestamp >= :dayStart AND timestamp < :dayEnd")
    suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double?

    @Query("SELECT * FROM expenses WHERE synced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsynced(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getExpensesByUser(userId: Long): List<ExpenseEntity>

    @Query(
        """
        SELECT u.name as staffName, SUM(e.amount) as totalUsed
        FROM expenses e 
        INNER JOIN users u ON u.userId = e.userId
        GROUP BY e.userId
    """
    )
    suspend fun getTotalUsedByStaff(): List<StaffUsageSummary>

    @Query("""
    SELECT strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) as month, 
           SUM(amount) as total
    FROM expenses
    WHERE userId = :userId 
      AND timestamp BETWEEN :from AND :to
    GROUP BY strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch'))
""")
    fun getMonthlyExpenseTrend(userId: Long, from: Long, to: Long): Flow<List<MonthlyExpenseTrend>>


    // Group expenses by category and calculate total spending for a user in a given date range
    @Query(
        """
    SELECT c.name AS categoryName, SUM(e.amount) AS total
    FROM expenses e
    LEFT JOIN categories c ON c.categoryId = e.categoryId
    WHERE e.userId = :userId AND e.timestamp BETWEEN :from AND :to
    GROUP BY e.categoryId
    ORDER BY total DESC
"""
    )
    fun getCategoryWiseSpending(
        userId: Long,
        from: Long,
        to: Long
    ): Flow<List<CategorySpendingSummary>>

    @Query("SELECT * FROM expenses WHERE status = 'Pending'")
    suspend fun getPendingExpenses(): List<ExpenseEntity>

    @Query("UPDATE expenses SET status = :status, approvedBy = :approverId WHERE expenseId = :expenseId")
    suspend fun updateApprovalStatus(expenseId: Long, status: String, approverId: Long?)
}