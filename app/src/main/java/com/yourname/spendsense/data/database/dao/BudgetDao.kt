package com.yourname.spendsense.data.database.dao

import androidx.room.*
import com.yourname.spendsense.data.database.entities.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budget WHERE month = :month")
    fun getBudgetForMonth(month: String): Flow<Budget?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)
}
