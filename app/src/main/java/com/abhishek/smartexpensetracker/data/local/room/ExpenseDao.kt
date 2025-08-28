package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
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

    @Query("""
        SELECT u.name as staffName, SUM(e.amount) as totalUsed
        FROM expenses e 
        INNER JOIN users u ON u.id = e.userId
        GROUP BY e.userId
    """)
    suspend fun getTotalUsedByStaff(): List<StaffUsageSummary>

    @Query("""
        SELECT strftime('%m', datetime(timestamp/1000, 'unixepoch')) as month, SUM(amount) as total
        FROM expenses
        WHERE userId = :userId
        GROUP BY strftime('%m', datetime(timestamp/1000, 'unixepoch'))
    """)
    suspend fun getMonthlyExpenseTrend(userId: Long): List<MonthlyExpenseTrend>

//    @Query("""
//        SELECT c.name as categoryName, SUM(e.amount) as total
//        FROM expenses e
//        LEFT JOIN categories c ON c.id = e.categoryId
//        WHERE e.userId = :userId
//        GROUP BY c.name
//    """)
//    suspend fun getCategoryWiseSpending(userId: Long): List<CategorySpendingSummary>

    @Query("SELECT * FROM expenses WHERE status = 'Pending'")
    suspend fun getPendingExpenses(): List<ExpenseEntity>

    @Query("UPDATE expenses SET status = :status, approvedBy = :approverId WHERE id = :expenseId")
    suspend fun updateApprovalStatus(expenseId: Long, status: String, approverId: Long?)
}