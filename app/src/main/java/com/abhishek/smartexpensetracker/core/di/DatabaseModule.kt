package com.abhishek.smartexpensetracker.core.di

import android.content.Context
import androidx.room.Room
import com.abhishek.smartexpensetracker.data.local.room.AppDatabase
import com.abhishek.smartexpensetracker.data.local.room.ExpenseDao
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.repository.ExpenseRepository
import com.abhishek.smartexpensetracker.data.repository.IExpenseRepository
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
    fun provideDatabase(@ApplicationContext context: Context) : AppDatabase{

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_expense_db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(false)
            .build()

    }

    @Provides
    @Singleton
    fun provideExpenseDao(db : AppDatabase) : ExpenseDao {
        return db.expenseDao()

    }

    @Provides
    @Singleton
    fun provideExpenseEntity() = Expense()


    @Provides
    @Singleton
    fun provideExpenseRepository(
        dao: ExpenseDao
    ): IExpenseRepository {
        return ExpenseRepository(dao)
    }
}
