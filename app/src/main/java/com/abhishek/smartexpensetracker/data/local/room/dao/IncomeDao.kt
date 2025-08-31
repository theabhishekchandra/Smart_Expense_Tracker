package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.*
import com.abhishek.smartexpensetracker.data.local.room.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    /** Income Show -> How much amount you earn for your business or personal use.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: IncomeEntity)

    @Update
    suspend fun update(income: IncomeEntity)

    @Delete
    suspend fun delete(income: IncomeEntity)

    @Query("SELECT * FROM incomes WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllForUser(userId: Long): Flow<List<IncomeEntity>>
}
