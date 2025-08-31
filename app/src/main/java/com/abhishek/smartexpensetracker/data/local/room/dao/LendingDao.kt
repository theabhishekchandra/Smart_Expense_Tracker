package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.*
import com.abhishek.smartexpensetracker.data.local.room.entity.LendingTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LendingDao {
    /** Lending show- > Your credit history.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lending: LendingTransactionEntity): Long

    @Update
    suspend fun update(lending: LendingTransactionEntity)

    @Delete
    suspend fun delete(lending: LendingTransactionEntity)

}
