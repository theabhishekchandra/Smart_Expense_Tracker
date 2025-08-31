package com.abhishek.smartexpensetracker.core.di

import android.content.Context
import androidx.room.Room
import com.abhishek.smartexpensetracker.data.local.room.*
import com.abhishek.smartexpensetracker.data.local.room.dao.AllocationDao
import com.abhishek.smartexpensetracker.data.local.room.dao.BudgetDao
import com.abhishek.smartexpensetracker.data.local.room.dao.CategoryDao
import com.abhishek.smartexpensetracker.data.local.room.dao.ExpenseDao
import com.abhishek.smartexpensetracker.data.local.room.dao.IncomeDao
import com.abhishek.smartexpensetracker.data.local.room.dao.LendingDao
import com.abhishek.smartexpensetracker.data.local.room.dao.NotificationDao
import com.abhishek.smartexpensetracker.data.local.room.dao.RepaymentDao
import com.abhishek.smartexpensetracker.data.local.room.dao.StaffDao
import com.abhishek.smartexpensetracker.data.local.room.dao.SyncLogDao
import com.abhishek.smartexpensetracker.data.local.room.dao.UserDao
import com.abhishek.smartexpensetracker.data.repository.*
import com.abhishek.smartexpensetracker.data.repository.local.CategoryRepository
import com.abhishek.smartexpensetracker.data.repository.local.ICategoryRepository
import com.abhishek.smartexpensetracker.data.repository.local.IStaffRepository
import com.abhishek.smartexpensetracker.data.repository.local.IUserRepository
import com.abhishek.smartexpensetracker.data.repository.local.StaffRepository
import com.abhishek.smartexpensetracker.data.repository.local.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_expense_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    // ---------- DAOs ----------

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun provideAllocationDao(db: AppDatabase): AllocationDao = db.allocationDao()

    @Provides
    @Singleton
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()

    @Provides
    @Singleton
    fun provideSyncLogDao(db: AppDatabase): SyncLogDao = db.syncLogDao()

    @Provides
    @Singleton
    fun provideStaffDao(db: AppDatabase): StaffDao = db.staffDao()

    @Provides
    @Singleton
    fun provideContactDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideIncomeDao(db: AppDatabase): IncomeDao = db.incomeDao()

    @Provides
    @Singleton
    fun provideLendingDao(db: AppDatabase): LendingDao = db.lendingDao()

    @Provides
    @Singleton
    fun provideRepaymentDao(db: AppDatabase): RepaymentDao = db.repaymentDao()

    // ---------- Repositories ----------

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): IExpenseRepository =
        ExpenseRepository(dao)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): IUserRepository = UserRepository(dao)
    @Provides
    @Singleton
    fun provideAllocationRepository(dao: AllocationDao): IAllocationRepository = AllocationRepository(dao)

    @Provides
    @Singleton
    fun provideBudgetRepository(dao: BudgetDao): IBudgetRepository = BudgetRepository(dao)

    @Provides
    @Singleton
    fun provideCategoryRepository(dao: CategoryDao): ICategoryRepository = CategoryRepository(dao)

    @Provides
    @Singleton
    fun provideNotificationRepository(dao: NotificationDao): INotificationRepository = NotificationRepository(dao)

    @Provides
    @Singleton
    fun provideSyncLogRepository(dao: SyncLogDao): ISyncLogRepository = SyncLogRepository(dao)


    @Provides
    @Singleton
    fun provideStaffRepository(dao: StaffDao, allocationDao: AllocationDao): IStaffRepository =
        StaffRepository(dao, allocationDao)



}
