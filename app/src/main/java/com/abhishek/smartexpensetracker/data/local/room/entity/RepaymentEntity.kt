package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "repayments",
    foreignKeys = [
        ForeignKey(entity = LendingTransactionEntity::class, parentColumns = ["lendingId"], childColumns = ["lendingId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("lendingId")]
)
data class RepaymentEntity(
    @PrimaryKey(autoGenerate = true) val repaymentId: Long = 0,
    val lendingId: Long,
    val amountPaid: Double,
    val date: Long = System.currentTimeMillis(),
    val paymentMethod: String? = null,
    val notes: String? = null
)