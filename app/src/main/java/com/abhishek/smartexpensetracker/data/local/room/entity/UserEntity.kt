package com.abhishek.smartexpensetracker.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. User Table (for Admin & Staff login)
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String?,
    val role: String = "user", // "admin"/"staff"/"viewer"/"approver"/"user"
    val phone: String? = null,
    val profilePicUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)