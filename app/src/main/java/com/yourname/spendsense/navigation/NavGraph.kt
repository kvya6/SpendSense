package com.yourname.spendsense.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yourname.spendsense.ui.screens.*
import com.yourname.spendsense.viewmodel.AuthViewModel
import com.yourname.spendsense.viewmodel.FinanceViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")
    object AddIncome : Screen("add_income")
    object Analytics : Screen("analytics")
    object Budget : Screen("budget")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    financeViewModel: FinanceViewModel,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication Screens
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Screens
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = financeViewModel,
                onNavigateToAddExpense = {
                    navController.navigate(Screen.AddExpense.route)
                },
                onNavigateToAddIncome = {
                    navController.navigate(Screen.AddIncome.route)
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onNavigateToBudget = {
                    navController.navigate(Screen.Budget.route)
                },
                onLogout = {
                    // Clear user session
                    authViewModel.logout()

                    // Navigate to login and clear back stack
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }

        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                viewModel = financeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddIncome.route) {
            AddIncomeScreen(
                viewModel = financeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                viewModel = financeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Budget.route) {
            BudgetScreen(
                viewModel = financeViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}