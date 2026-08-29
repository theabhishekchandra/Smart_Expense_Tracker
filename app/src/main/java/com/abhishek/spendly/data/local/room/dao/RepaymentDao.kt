package com.abhishek.spendly.data.local.room.dao

import androidx.room.*
import com.abhishek.spendly.data.local.room.entity.RepaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repayment: RepaymentEntity): Long

    @Update
    suspend fun update(repayment: RepaymentEntity)

    @Delete
    suspend fun delete(repayment: RepaymentEntity)

    @Query("SELECT * FROM repayments WHERE lendingId = :lendingId ORDER BY date DESC")
    fun getForLending(lendingId: Long): Flow<List<RepaymentEntity>>

    // sum repayments for a lending id
    @Query("SELECT IFNULL(SUM(amountPaid),0) FROM repayments WHERE lendingId = :lendingId")
    suspend fun getTotalRepaymentsForLending(lendingId: Long): Double
}
