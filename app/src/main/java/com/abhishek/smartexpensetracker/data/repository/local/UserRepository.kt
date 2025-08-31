package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.dao.UserDao
import com.abhishek.smartexpensetracker.data.local.room.entity.StaffExpenseSummary
import com.abhishek.smartexpensetracker.data.local.room.entity.UserEntity
import javax.inject.Inject

interface IUserRepository {
    suspend fun insertUser(user: UserEntity)
    suspend fun getAdmin(): UserEntity?
    suspend fun getAllStaff(): List<UserEntity>
//    suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary>
    suspend fun getUserById(id: Long): UserEntity?
    suspend fun deactivateUser(id: Long)
}

class UserRepository @Inject constructor(
    private val userDao: UserDao
) : IUserRepository {
    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    override suspend fun getAdmin(): UserEntity? = userDao.getAdmin()
    override suspend fun getAllStaff(): List<UserEntity> = userDao.getAllStaff()
//    override suspend fun getStaffWithExpenseSummary(): List<StaffExpenseSummary> = userDao.getStaffWithExpenseSummary()
    override suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)
    override suspend fun deactivateUser(id: Long) = userDao.deactivateUser(id)


//    suspend fun update(user: UserEntity) = userDao.update(user)
//
//    fun observeUser(userId: Long): Flow<UserEntity?> = userDao.getById(userId)
//
//    suspend fun findByEmail(email: String): UserEntity? = userDao.getByEmailSuspend(email)
//
//    suspend fun delete(userId: Long) = userDao.deleteById(userId)
}
