package com.abhishek.spendly.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "lendings",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ContactEntity::class, parentColumns = ["contactId"], childColumns = ["contactId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("contactId")]
)
data class LendingTransactionEntity(
    @PrimaryKey(autoGenerate = true) val lendingId: Long = 0,
    val userId: Long?,
    val contactId: Long,
    val amount: Double,
    val transactionType: String, // "lent" or "borrowed" or "udhar_sale"
    val dueDate: Long? = null,
    val status: String = "pending", // pending / partial / paid / overdue
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)