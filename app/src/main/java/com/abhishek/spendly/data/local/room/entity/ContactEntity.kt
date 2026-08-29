package com.abhishek.spendly.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val contactId: Long = 0,
    val userId: Long?,
    val name: String,
    val phone: String?,
    val email: String? = null,
    val address: String? = null,
    val type: String = "both", // lender / borrower / both / customer
    val createdAt: Long = System.currentTimeMillis()
)