package com.abhishek.spendly.data.repository.local

import com.abhishek.spendly.data.local.room.dao.ExpenseDao
import com.abhishek.spendly.data.local.room.dao.IncomeDao
import com.abhishek.spendly.data.local.room.dao.LendingDao
import com.abhishek.spendly.data.local.room.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ProfitLoss(
    val income: Double,
    val expense: Double,
    val profit: Double
)


class ReportRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val incomeDao: IncomeDao,
    private val lendingDao: LendingDao
) : IReportRepository{

    /** Profit & Loss between [from, to] */
    override suspend fun observeProfitLoss(userId: Long, from: Long, to: Long): Flow<ProfitLoss> {
        val expenseSumFlow = expenseDao.getAll().map { list ->
            list.filter { it.userId == userId && it.timestamp in from..to }
                .sumOf { it.amount }
        }

        val incomeSumFlow = incomeDao.getAllForUser(userId).map { list ->
            list.filter { it.timestamp in from..to }
                .sumOf { it.amount }
        }

        return incomeSumFlow.combine(expenseSumFlow) { inc, exp ->
            ProfitLoss(inc, exp, inc - exp)
        }
    }

    /** Daily expenses for a given day */
    override suspend fun observeDailyExpenses(dayStart: Long, dayEnd: Long): Flow<Double> =
        expenseDao.getForDayRange(dayStart, dayEnd).map { list ->
            list.sumOf { it.amount }
        }

    /** Monthly expense trend for chart view */
    override suspend fun observeMonthlyExpenseTrend(userId: Long, from: Long, to: Long): Flow<List<MonthlyExpenseTrend>> =
        expenseDao.getMonthlyExpenseTrend(userId, from, to)

    /** Category-wise spending breakdown (chart/pie) */
    override suspend fun observeCategoryBreakdown(userId: Long, from: Long, to: Long): Flow<List<CategorySpendingSummary>> =
        expenseDao.getCategoryWiseSpending(userId, from, to)

    /** Staff usage report (business use case) */
    override suspend fun getStaffUsageSummary(): List<StaffUsageSummary> =
        expenseDao.getTotalUsedByStaff()

    /** Pending expenses that need approval */
    override suspend fun getPendingExpenses(): List<ExpenseEntity> =
        expenseDao.getPendingExpenses()

    /** Credit / Udhar outstanding total */
//    override suspend fun observePendingCredit(userId: Long): Flow<Double> =
//        lendingDao.getTotalPendingForUser(userId)

    /** Dashboard summary combining P&L, credit, and monthly trend */
    /*override suspend fun observeDashboardSummary(
        userId: Long,
        from: Long,
        to: Long
    ): Flow<DashboardSummary> {
        val profitLossFlow = observeProfitLoss(userId, from, to)
        val pendingCreditFlow = observePendingCredit(userId)
        val monthlyTrendFlow = observeMonthlyExpenseTrend(userId, from, to)

        return combine(
            profitLossFlow,
            pendingCreditFlow,
            monthlyTrendFlow
        ) { pl, credit, trend ->
            DashboardSummary(
                profitLoss = pl,
                pendingCredit = credit,
                monthlyTrend = trend
            )
        }
    }*/
}

interface IReportRepository{
    suspend fun observeProfitLoss(userId: Long, from: Long, to: Long): Flow<ProfitLoss>
    suspend fun observeDailyExpenses(dayStart: Long, dayEnd: Long): Flow<Double>
    suspend fun observeMonthlyExpenseTrend(userId: Long, from: Long, to: Long): Flow<List<MonthlyExpenseTrend>>
    suspend fun observeCategoryBreakdown(userId: Long, from: Long, to: Long): Flow<List<CategorySpendingSummary>>
    suspend fun getStaffUsageSummary(): List<StaffUsageSummary>
    suspend fun getPendingExpenses(): List<ExpenseEntity>
//    suspend fun observePendingCredit(userId: Long): Flow<Double>
//    suspend fun observeDashboardSummary(userId: Long,from: Long,to: Long): Flow<DashboardSummary>
}


/** Aggregated dashboard summary for quick screen load */
data class DashboardSummary(
    val profitLoss: ProfitLoss,
    val pendingCredit: Double,
    val monthlyTrend: List<MonthlyExpenseTrend>
)
