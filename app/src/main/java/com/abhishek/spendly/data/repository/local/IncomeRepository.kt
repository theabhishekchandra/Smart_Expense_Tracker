package com.abhishek.spendly.data.repository.local

import com.abhishek.spendly.data.local.room.dao.IncomeDao
import com.abhishek.spendly.data.local.room.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao
) : IIncomeRepository{
    override suspend fun add(income: IncomeEntity) = incomeDao.insert(income)
    override suspend fun update(income: IncomeEntity) = incomeDao.update(income)
    override suspend fun delete(income: IncomeEntity) = incomeDao.delete(income)
    override fun observeAllForUser(userId: Long): Flow<List<IncomeEntity>> = incomeDao.getAllForUser(userId)
}

interface IIncomeRepository{
    suspend fun add(income: IncomeEntity)
    suspend fun update(income: IncomeEntity)
    suspend fun delete(income: IncomeEntity)
    fun observeAllForUser(userId: Long): Flow<List<IncomeEntity>>

}