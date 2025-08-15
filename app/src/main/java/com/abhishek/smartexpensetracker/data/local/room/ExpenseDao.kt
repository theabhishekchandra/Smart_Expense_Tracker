package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.*
import com.abhishek.smartexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE timestamp >= :dayStart AND timestamp < :dayEnd ORDER BY timestamp DESC")
    fun getForDayRange(dayStart: Long, dayEnd: Long): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE timestamp >= :dayStart AND timestamp < :dayEnd")
    suspend fun totalForDayRange(dayStart: Long, dayEnd: Long): Double?

    @Query("SELECT * FROM expenses WHERE synced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsynced(): List<Expense>
}
