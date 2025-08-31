package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhishek.smartexpensetracker.data.local.room.dao.AllocationDao
import com.abhishek.smartexpensetracker.data.local.room.dao.BudgetDao
import com.abhishek.smartexpensetracker.data.local.room.dao.CategoryDao
import com.abhishek.smartexpensetracker.data.local.room.dao.ContactDao
import com.abhishek.smartexpensetracker.data.local.room.dao.ExpenseDao
import com.abhishek.smartexpensetracker.data.local.room.dao.IncomeDao
import com.abhishek.smartexpensetracker.data.local.room.dao.LendingDao
import com.abhishek.smartexpensetracker.data.local.room.dao.NotificationDao
import com.abhishek.smartexpensetracker.data.local.room.dao.RepaymentDao
import com.abhishek.smartexpensetracker.data.local.room.dao.StaffDao
import com.abhishek.smartexpensetracker.data.local.room.dao.SyncLogDao
import com.abhishek.smartexpensetracker.data.local.room.dao.UserDao
import com.abhishek.smartexpensetracker.data.local.room.entity.AllocationEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.BudgetEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.CategoryEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.ContactEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.ExpenseEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.IncomeEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.LendingTransactionEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.NotificationEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.RepaymentEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.StaffEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.SyncLogEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.UserEntity

@Database(
    entities = [
        AllocationEntity::class,
        BudgetEntity::class,
        CategoryEntity::class,
        ContactEntity::class,
        ExpenseEntity::class,
        IncomeEntity::class,
        LendingTransactionEntity::class,
        NotificationEntity::class,
        RepaymentEntity::class,
        StaffEntity::class,
        SyncLogEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allocationDao(): AllocationDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun contactDao(): ContactDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao() : IncomeDao
    abstract fun lendingDao() : LendingDao
    abstract fun notificationDao(): NotificationDao
    abstract fun repaymentDao(): RepaymentDao
    abstract fun staffDao(): StaffDao
    abstract fun syncLogDao(): SyncLogDao
    abstract fun userDao(): UserDao
}
