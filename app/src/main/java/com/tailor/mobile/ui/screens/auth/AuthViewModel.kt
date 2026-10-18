package com.tailor.mobile.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tailor.mobile.data.repository.AuthRepository
import com.tailor.mobile.data.source.remote.response.UserDto
import com.tailor.mobile.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, List<String>>? = null,
    val isSuccess: Boolean = false,
    val user: UserDto? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository.create(application)

    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    val isLoggedIn = authRepository.isLoggedIn

    fun onEmailChange(newEmail: String) {
        _loginState.update { it.copy(email = newEmail, errorMessage = null, fieldErrors = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _loginState.update { it.copy(password = newPassword, errorMessage = null, fieldErrors = null) }
    }

    fun login() {
        val email = _loginState.value.email.trim()
        val password = _loginState.value.password

        if (email.isBlank()) {
            _loginState.update { it.copy(errorMessage = "Email wajib diisi.") }
            return
        }
        if (password.isBlank()) {
            _loginState.update { it.copy(errorMessage = "Password wajib diisi.") }
            return
        }

        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _loginState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _loginState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                user = resource.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _loginState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message,
                                fieldErrors = resource.errors
                            )
                        }
                    }
                }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout().collect {
                _loginState.value = LoginUiState()
                onLoggedOut()
            }
        }
    }

    fun resetState() {
        _loginState.update { it.copy(isSuccess = false, errorMessage = null) }
    }
}
