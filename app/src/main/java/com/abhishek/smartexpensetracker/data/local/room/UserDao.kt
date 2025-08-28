package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE role = 'admin' LIMIT 1")
    suspend fun getAdmin(): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'staff'")
    suspend fun getAllStaff(): List<UserEntity>

    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT u.*, 
        (SELECT IFNULL(SUM(amount),0) FROM expenses e WHERE e.userId = u.id) as totalExpenses
        FROM users u
        WHERE u.role = 'staff'
    """)
    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("UPDATE users SET isActive = 0 WHERE id = :id")
    suspend fun deactivateUser(id: Long)
}
