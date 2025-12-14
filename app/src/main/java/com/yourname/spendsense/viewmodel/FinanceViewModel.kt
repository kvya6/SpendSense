package com.yourname.spendsense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.spendsense.data.database.entities.Budget
import com.yourname.spendsense.data.database.entities.Expense
import com.yourname.spendsense.data.database.entities.Income
import com.yourname.spendsense.data.repository.FinanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class FinanceViewModel(private val repository: FinanceRepository) : ViewModel() {

    // StateFlows for UI
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _income = MutableStateFlow<List<Income>>(emptyList())
    val income: StateFlow<List<Income>> = _income.asStateFlow()

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    // Computed totals - updates automatically when expenses/income change
    val totalExpenses: StateFlow<Double> = _expenses
        .map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val totalIncome: StateFlow<Double> = _income
        .map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val balance: StateFlow<Double> = combine(totalIncome, totalExpenses) { income, expenses ->
        income - expenses
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    init {
        loadData()
    }

    private fun loadData() {
        // Load expenses
        viewModelScope.launch {
            repository.getAllExpenses().collect { expenseList ->
                _expenses.value = expenseList
            }
        }

        // Load income
        viewModelScope.launch {
            repository.getAllIncome().collect { incomeList ->
                _income.value = incomeList
            }
        }

        // Load budgets
        viewModelScope.launch {
            repository.getAllBudgets().collect { budgetList ->
                _budgets.value = budgetList
            }
        }
    }

    // Expense operations with instant UI updates
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            // Optimistically update UI first
            _expenses.value = _expenses.value + expense

            // Then save to database
            try {
                repository.insertExpense(expense)
            } catch (e: Exception) {
                // If save fails, revert the UI update
                _expenses.value = _expenses.value - expense
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            // Optimistically update UI first
            _expenses.value = _expenses.value - expense

            // Then delete from database
            try {
                repository.deleteExpense(expense)
            } catch (e: Exception) {
                // If delete fails, revert the UI update
                _expenses.value = _expenses.value + expense
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    // Income operations with instant UI updates
    fun addIncome(income: Income) {
        viewModelScope.launch {
            // Optimistically update UI first
            _income.value = _income.value + income

            // Then save to database
            try {
                repository.insertIncome(income)
            } catch (e: Exception) {
                // If save fails, revert the UI update
                _income.value = _income.value - income
            }
        }
    }

    fun deleteIncome(income: Income) {
        viewModelScope.launch {
            // Optimistically update UI first
            _income.value = _income.value - income

            // Then delete from database
            try {
                repository.deleteIncome(income)
            } catch (e: Exception) {
                // If delete fails, revert the UI update
                _income.value = _income.value + income
            }
        }
    }

    // Budget operations with instant UI updates
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            // Optimistically update UI first
            val existingBudget = _budgets.value.find { it.month == budget.month }
            if (existingBudget != null) {
                _budgets.value = _budgets.value.map {
                    if (it.month == budget.month) budget else it
                }
            } else {
                _budgets.value = _budgets.value + budget
            }

            // Then save to database
            try {
                repository.insertBudget(budget)
            } catch (e: Exception) {
                // Revert on failure
                loadData()
            }
        }
    }

    // Helper methods for calculations
    fun getCategoryExpenses(category: String): Double {
        return _expenses.value
            .filter { it.category == category }
            .sumOf { it.amount }
    }

    fun getBalance(): Double {
        return totalIncome.value - totalExpenses.value
    }

    fun getMonthlyExpenses(): Double {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startOfMonth = calendar.timeInMillis

        return _expenses.value
            .filter { it.date >= startOfMonth }
            .sumOf { it.amount }
    }

    fun getMonthlyIncome(): Double {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startOfMonth = calendar.timeInMillis

        return _income.value
            .filter { it.date >= startOfMonth }
            .sumOf { it.amount }
    }
}
