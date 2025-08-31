package com.abhishek.smartexpensetracker.data.repository.local

import androidx.room.withTransaction
import com.abhishek.smartexpensetracker.data.local.room.*
import com.abhishek.smartexpensetracker.data.local.room.dao.LendingDao
import com.abhishek.smartexpensetracker.data.local.room.dao.RepaymentDao
import com.abhishek.smartexpensetracker.data.local.room.entity.LendingTransactionEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.RepaymentEntity
import kotlinx.coroutines.flow.Flow

class LendingRepository(
    private val db: AppDatabase,
    private val lendingDao: LendingDao,
    private val repaymentDao: RepaymentDao,
): ILendingRepository {
    // --- Lending CRUD ---
    override suspend fun addLending(txn: LendingTransactionEntity): Long = lendingDao.insert(txn)
    override suspend fun updateLending(txn: LendingTransactionEntity) = lendingDao.update(txn)
    override suspend fun deleteLending(txn: LendingTransactionEntity) = lendingDao.delete(txn)

//    override suspend fun observeAllForUser(userId: Long): Flow<List<LendingTransactionEntity>> =
//        lendingDao.getAllForUser(userId)

//    override suspend fun observeForContact(contactId: Long): Flow<List<LendingTransactionEntity>> =
//        lendingDao.getForContact(contactId)

//    override suspend fun observeTotalPending(userId: Long): Flow<Double> = lendingDao.getTotalPendingForUser(userId)

    // --- Repayments ---
    override suspend fun addRepayment(rep: RepaymentEntity): Long = repaymentDao.insert(rep)
    override suspend fun updateRepayment(rep: RepaymentEntity) = repaymentDao.update(rep)
    override suspend fun deleteRepayment(rep: RepaymentEntity) = repaymentDao.delete(rep)
    override suspend fun observeRepayments(lendingId: Long): Flow<List<RepaymentEntity>> =
        repaymentDao.getForLending(lendingId)

    // --- Domain helpers ---
    /** Records a repayment and auto-marks lending as paid/partial */
    override suspend fun repayAndUpdateStatus(lending: LendingTransactionEntity, payAmount: Double) {
        require(payAmount > 0) { "Repayment amount must be > 0" }

        db.withTransaction {
            repaymentDao.insert(
                RepaymentEntity(
                    lendingId = lending.lendingId,
                    amountPaid = payAmount
                )
            )
            val totalRepaid = repaymentDao.getTotalRepaymentsForLending(lending.lendingId)
            val newStatus = when {
                totalRepaid <= 0.0 -> "pending"
                totalRepaid < lending.amount -> "partial"
                else -> "paid"
            }
            lendingDao.update(lending.copy(status = newStatus))
        }
    }
}

interface ILendingRepository{
    suspend fun addLending(txn: LendingTransactionEntity): Long
    suspend fun updateLending(txn: LendingTransactionEntity)
    suspend fun deleteLending(txn: LendingTransactionEntity)
//    suspend fun observeAllForUser(userId: Long): Flow<List<LendingTransactionEntity>>
//    suspend fun observeForContact(contactId: Long): Flow<List<LendingTransactionEntity>>
//    suspend fun observeTotalPending(userId: Long): Flow<Double>
    suspend fun addRepayment(rep: RepaymentEntity): Long
    suspend fun updateRepayment(rep: RepaymentEntity)
    suspend fun deleteRepayment(rep: RepaymentEntity)
    suspend fun observeRepayments(lendingId: Long): Flow<List<RepaymentEntity>>
    suspend fun repayAndUpdateStatus(lending: LendingTransactionEntity, payAmount: Double)

}

