package com.abhishek.spendly.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abhishek.spendly.data.local.room.*
import com.abhishek.spendly.data.local.room.dao.AllocationDao
import com.abhishek.spendly.data.local.room.dao.BudgetDao
import com.abhishek.spendly.data.local.room.dao.CategoryDao
import com.abhishek.spendly.data.local.room.dao.ContactDao
import com.abhishek.spendly.data.local.room.dao.ExpenseDao
import com.abhishek.spendly.data.local.room.dao.IncomeDao
import com.abhishek.spendly.data.local.room.dao.LendingDao
import com.abhishek.spendly.data.local.room.dao.NotificationDao
import com.abhishek.spendly.data.local.room.dao.RepaymentDao
import com.abhishek.spendly.data.local.room.dao.StaffDao
import com.abhishek.spendly.data.local.room.dao.SyncLogDao
import com.abhishek.spendly.data.local.room.dao.UserDao
import com.abhishek.spendly.data.repository.*
import com.abhishek.spendly.data.repository.local.CategoryRepository
import com.abhishek.spendly.data.repository.local.ContactRepository
import com.abhishek.spendly.data.repository.local.ICategoryRepository
import com.abhishek.spendly.data.repository.local.IContactRepository
import com.abhishek.spendly.data.repository.local.IIncomeRepository
import com.abhishek.spendly.data.repository.local.ILendingRepository
import com.abhishek.spendly.data.repository.local.IReportRepository
import com.abhishek.spendly.data.repository.local.IStaffRepository
import com.abhishek.spendly.data.repository.local.IUserRepository
import com.abhishek.spendly.data.repository.local.IncomeRepository
import com.abhishek.spendly.data.repository.local.LendingRepository
import com.abhishek.spendly.data.repository.local.ReportRepository
import com.abhishek.spendly.data.repository.local.StaffRepository
import com.abhishek.spendly.data.repository.local.UserRepository
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
            // Schema history was never captured for versions 1-3 (no app/schemas/*.json exists
            // for them), so a real Migration can't be written for upgrades from those versions.
            // Destructive fallback is scoped to ONLY those legacy versions; any future version
            // bump (5+) must ship a real Migration or this build will fail at runtime.
            .fallbackToDestructiveMigrationFrom(1, 2, 3)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Seed a default local device user (userId = 1) so expenses/income/lending
                    // records always have a valid owner FK - the app has no real multi-user
                    // auth/account-creation flow wired up yet.
                    db.execSQL(
                        "INSERT INTO users (userId, name, email, passwordHash, role, phone, profilePicUri, createdAt, isActive) " +
                            "VALUES (1, 'Me', '', NULL, 'user', NULL, NULL, ${System.currentTimeMillis()}, 1)"
                    )
                }
            })
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
    fun provideContactDao(db: AppDatabase): ContactDao = db.contactDao()

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

    @Provides
    @Singleton
    fun provideContactRepository(dao: ContactDao): IContactRepository = ContactRepository(dao)

    @Provides
    @Singleton
    fun provideIncomeRepository(dao: IncomeDao): IIncomeRepository = IncomeRepository(dao)

    @Provides
    @Singleton
    fun provideLendingRepository(
        db: AppDatabase,
        lendingDao: LendingDao,
        repaymentDao: RepaymentDao
    ): ILendingRepository = LendingRepository(db, lendingDao, repaymentDao)

    @Provides
    @Singleton
    fun provideReportRepository(
        expenseDao: ExpenseDao,
        incomeDao: IncomeDao,
        lendingDao: LendingDao
    ): IReportRepository = ReportRepository(expenseDao, incomeDao, lendingDao)

}
