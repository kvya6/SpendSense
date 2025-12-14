package com.yourname.spendsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.yourname.spendsense.data.database.AppDatabase
import com.yourname.spendsense.data.local.UserPreferences
import com.yourname.spendsense.data.repository.FinanceRepository
import com.yourname.spendsense.navigation.NavGraph
import com.yourname.spendsense.navigation.Screen
import com.yourname.spendsense.ui.theme.SpendSenseTheme
import com.yourname.spendsense.viewmodel.AuthViewModel
import com.yourname.spendsense.viewmodel.AuthViewModelFactory
import com.yourname.spendsense.viewmodel.FinanceViewModel
import com.yourname.spendsense.viewmodel.FinanceViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var financeViewModel: FinanceViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize UserPreferences
        userPreferences = UserPreferences(applicationContext)

        // Initialize Database
        val database = AppDatabase.getDatabase(applicationContext)

        // Initialize Repository
        val repository = FinanceRepository(
            expenseDao = database.expenseDao(),
            incomeDao = database.incomeDao(),
            budgetDao = database.budgetDao(),
            userDao = database.userDao()
        )

        // Initialize ViewModels
        val financeFactory = FinanceViewModelFactory(repository)
        financeViewModel = ViewModelProvider(this, financeFactory)[FinanceViewModel::class.java]

        val authFactory = AuthViewModelFactory(repository, userPreferences)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        setContent {
            SpendSenseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Determine start destination based on login status
                    val startDestination = if (authViewModel.isLoggedIn()) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }

                    NavGraph(
                        navController = navController,
                        financeViewModel = financeViewModel,
                        authViewModel = authViewModel,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}