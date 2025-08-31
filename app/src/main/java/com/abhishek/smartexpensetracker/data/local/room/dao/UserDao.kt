package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.abhishek.smartexpensetracker.data.local.room.entity.StaffExpenseSummary
import com.abhishek.smartexpensetracker.data.local.room.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE role = 'admin' LIMIT 1")
    suspend fun getAdmin(): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'staff'")
    suspend fun getAllStaff(): List<UserEntity>

//    @RewriteQueriesToDropUnusedColumns
//    @Query(
//        """
//        SELECT u.*,
//        (SELECT IFNULL(SUM(amount),0) FROM expenses e WHERE e.userId = u.userId) as totalExpenses
//        FROM users u
//        WHERE u.role = 'staff'
//    """
//    )
//    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("UPDATE users SET isActive = 0 WHERE userId = :id")
    suspend fun deactivateUser(id: Long)
}
