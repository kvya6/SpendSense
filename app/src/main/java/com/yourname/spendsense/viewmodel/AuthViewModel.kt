package com.yourname.spendsense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.spendsense.data.database.entities.User
import com.yourname.spendsense.data.local.UserPreferences
import com.yourname.spendsense.data.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: FinanceRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun isLoggedIn(): Boolean = userPreferences.isLoggedIn()

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // Validation
            when {
                name.isBlank() -> {
                    _authState.value = AuthState.Error("Name cannot be empty")
                    return@launch
                }
                email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    _authState.value = AuthState.Error("Please enter a valid email")
                    return@launch
                }
                password.length < 6 -> {
                    _authState.value = AuthState.Error("Password must be at least 6 characters")
                    return@launch
                }
                password != confirmPassword -> {
                    _authState.value = AuthState.Error("Passwords do not match")
                    return@launch
                }
            }

            // Check if user already exists
            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                _authState.value = AuthState.Error("User already exists with this email")
                return@launch
            }

            // Create new user
            val user = User(
                name = name,
                email = email,
                password = password  // In production, hash this!
            )

            try {
                val userId = repository.registerUser(user)
                val savedUser = repository.getUserById(userId.toInt())

                if (savedUser != null) {
                    userPreferences.saveUserSession(savedUser.id, savedUser.name, savedUser.email)
                    _authState.value = AuthState.Success(savedUser)
                } else {
                    _authState.value = AuthState.Error("Failed to register user")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // Validation
            when {
                email.isBlank() -> {
                    _authState.value = AuthState.Error("Email cannot be empty")
                    return@launch
                }
                password.isBlank() -> {
                    _authState.value = AuthState.Error("Password cannot be empty")
                    return@launch
                }
            }

            try {
                val user = repository.loginUser(email, password)

                if (user != null) {
                    userPreferences.saveUserSession(user.id, user.name, user.email)
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        userPreferences.clearSession()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
