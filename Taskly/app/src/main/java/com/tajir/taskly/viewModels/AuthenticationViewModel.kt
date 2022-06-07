package com.tajir.taskly.viewModels

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajir.taskly.data.api.models.GeneralResponseAuth
import com.tajir.taskly.data.api.models.Login
import com.tajir.taskly.data.api.repository.AuthRepository
import com.tajir.taskly.data.api.models.Register
import com.tajir.taskly.data.stateModels.AuthenticationMode
import com.tajir.taskly.data.stateModels.AuthenticationState
import com.tajir.taskly.data.stateModels.PasswordRequirement
import com.tajir.taskly.events.AuthenticationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class AuthenticationViewModel : ViewModel() {
    val uiState = MutableStateFlow(AuthenticationState())

    private fun updateEmail(email: String) {
        dismissError()
        uiState.value = uiState.value.copy(
            email = email
        )
    }

    private fun updateFirstName(firstName: String) {
        dismissError()
        uiState.value = uiState.value.copy(
            first_name = firstName
        )
    }

    private fun updateLastName(lastName: String) {
        dismissError()
        uiState.value = uiState.value.copy(
            last_name = lastName
        )
    }

    private fun updateAuthResponse(res: Response<GeneralResponseAuth>) {
        uiState.value = uiState.value.copy(
            authResponse = res
        )
    }

    private fun updatePassword(password: String) {
        dismissError()
        val requirements = mutableListOf<PasswordRequirement>()
        if (password.length > 7) {
            requirements.add(PasswordRequirement.EIGHT_CHARACTERS)
        }
        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirement.CAPITAL_LETTER)
        }
        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirement.NUMBER)
        }
        uiState.value = uiState.value.copy(
            password = password,
            passwordRequirements = requirements.toList()
        )
    }

    private fun authenticate() {
        uiState.value = uiState.value.copy(
            isLoading = true
        )

        if (uiState.value.authenticationMode == AuthenticationMode.SIGN_UP) {
            register()
        } else {
            login()
        }
        uiState.value = uiState.value.copy(
            isLoading = false
        )
    }

    private fun register() {
        val data = Register(
            first_name = uiState.value.first_name ?: "",
            last_name = uiState.value.last_name ?: "",
            email = uiState.value.email ?: "",
            password = uiState.value.password ?: ""
        )
        val repo = AuthRepository()
        viewModelScope.launch {
            try {
                val res = repo.register(data = data)
                updateAuthResponse(res)
            } catch (e: Exception) {
                setError("Check Your Internet Connection.")
            }
        }
    }

    private fun login() {
        val data = Login(
            email = uiState.value.email ?: "",
            password = uiState.value.password ?: ""
        )
        val repo = AuthRepository()
        viewModelScope.launch {
            try {
                val res = repo.login(data = data)
                updateAuthResponse(res)
            } catch (e: Exception) {
                setError("Check Your Internet Connection.")
            }

        }
    }

    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null
        )
    }

    private fun setError(err: String) {
        uiState.value = uiState.value.copy(
            error = err
        )
    }

    private fun clearAuthResponse() {
        uiState.value = uiState.value.copy(
            authResponse = null
        )
    }

    private fun clearState() {
        uiState.value = uiState.value.copy(
            authenticationMode = AuthenticationMode.SIGN_IN,
            first_name = null,
            last_name = null,
            email = null,
            password = null,
            passwordRequirements = emptyList(),
            isLoading = false,
            authResponse = null,
            error = null
        )
        println("Hello")
        println(uiState.value)
    }

    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ToggleAuthenticationMode -> {
                toggleAuthenticationMode()
            }
            is AuthenticationEvent.FirstNameChanged -> {
                updateFirstName(authenticationEvent.firstName)
            }
            is AuthenticationEvent.LastNameChanged -> {
                updateLastName(authenticationEvent.lastName)
            }
            is AuthenticationEvent.EmailChanged -> {
                updateEmail(authenticationEvent.emailAddress)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(authenticationEvent.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate()
            }
            is AuthenticationEvent.ErrorDismissed -> {
                dismissError()
            }
            is AuthenticationEvent.ClearAuthResponse -> {
                clearAuthResponse()
            }
            is AuthenticationEvent.SetError -> {
                setError(authenticationEvent.error)
            }
            is AuthenticationEvent.Logout -> {
                clearState()
            }
        }
    }

    private fun toggleAuthenticationMode() {
        dismissError()
        val authenticationMode = uiState.value.authenticationMode
        val newAuthenticationMode = if (
            authenticationMode == AuthenticationMode.SIGN_IN
        ) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        uiState.value = uiState.value.copy(
            authenticationMode = newAuthenticationMode
        )
    }
}

val AuthState by lazy { compositionLocalOf<AuthenticationViewModel> { error("Authentication Context not found.") } }
