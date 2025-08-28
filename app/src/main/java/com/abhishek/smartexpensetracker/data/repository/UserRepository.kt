package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.UserDao
import com.abhishek.smartexpensetracker.data.local.room.UserEntity
import com.abhishek.smartexpensetracker.data.local.room.StaffExpenseSummary
import javax.inject.Inject

interface IUserRepository {
    suspend fun insertUser(user: UserEntity)
    suspend fun getAdmin(): UserEntity?
    suspend fun getAllStaff(): List<UserEntity>
    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>
    suspend fun getUserById(id: Long): UserEntity?
    suspend fun deactivateUser(id: Long)
}

class UserRepository @Inject constructor(
    private val dao: UserDao
) : IUserRepository {
    override suspend fun insertUser(user: UserEntity) = dao.insertUser(user)
    override suspend fun getAdmin(): UserEntity? = dao.getAdmin()
    override suspend fun getAllStaff(): List<UserEntity> = dao.getAllStaff()
    override suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary> = dao.getStaffWithExpenseSummary()
    override suspend fun getUserById(id: Long): UserEntity? = dao.getUserById(id)
    override suspend fun deactivateUser(id: Long) = dao.deactivateUser(id)
}
