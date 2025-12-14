package com.yourname.spendsense.data.repository

import com.yourname.spendsense.data.database.dao.BudgetDao
import com.yourname.spendsense.data.database.dao.ExpenseDao
import com.yourname.spendsense.data.database.dao.IncomeDao
import com.yourname.spendsense.data.database.dao.UserDao
import com.yourname.spendsense.data.database.entities.Budget
import com.yourname.spendsense.data.database.entities.Expense
import com.yourname.spendsense.data.database.entities.Income
import com.yourname.spendsense.data.database.entities.User
import kotlinx.coroutines.flow.Flow

class FinanceRepository(
    private val expenseDao: ExpenseDao,
    private val incomeDao: IncomeDao,
    private val budgetDao: BudgetDao,
    private val userDao: UserDao
) {
    // Expenses
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
    fun getTotalExpensesByDateRange(startDate: Long, endDate: Long): Flow<Double?> =
        expenseDao.getTotalExpensesByDateRange(startDate, endDate)
    fun getExpensesByCategory(category: String): Flow<List<Expense>> =
        expenseDao.getExpensesByCategory(category)

    // Income
    fun getAllIncome(): Flow<List<Income>> = incomeDao.getAllIncome()
    suspend fun insertIncome(income: Income) = incomeDao.insertIncome(income)
    suspend fun updateIncome(income: Income) = incomeDao.updateIncome(income)
    suspend fun deleteIncome(income: Income) = incomeDao.deleteIncome(income)
    fun getTotalIncomeByDateRange(startDate: Long, endDate: Long): Flow<Double?> =
        incomeDao.getTotalIncomeByDateRange(startDate, endDate)

    // Budget
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()
    fun getBudgetForMonth(month: String): Flow<Budget?> = budgetDao.getBudgetForMonth(month)
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)
    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)
    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)

    // User Authentication
    suspend fun registerUser(user: User): Long = userDao.insertUser(user)
    suspend fun loginUser(email: String, password: String): User? =
        userDao.login(email, password)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
}