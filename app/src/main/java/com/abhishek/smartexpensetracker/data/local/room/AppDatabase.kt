package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        ExpenseEntity::class,
        AllocationEntity::class,
        BudgetEntity::class,
        Category::class,
        Notification::class,
        SyncLog::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun allocationDao(): AllocationDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun notificationDao(): NotificationDao
    abstract fun syncLogDao(): SyncLogDao
    abstract fun staffDao(): StaffDao
}
