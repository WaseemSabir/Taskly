package com.tajir.taskly.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tajir.taskly.data.models.AuthenticationState
import com.tajir.taskly.events.AuthenticationEvent

@Composable
fun AuthenticationComponent(
    modifier: Modifier = Modifier,
    authenticationState: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthenticationForm(
                modifier = Modifier.fillMaxSize(),
                authenticationMode = authenticationState.authenticationMode,
                email = authenticationState.email,
                password = authenticationState.password,
                onEmailChanged = {
                    handleEvent(AuthenticationEvent.EmailChanged(it))
                },
                onPasswordChanged = {
                    handleEvent(
                        AuthenticationEvent.PasswordChanged(it)
                    )
                },
                onAuthenticate = {
                    handleEvent(AuthenticationEvent.Authenticate)
                },
                completedPasswordRequirements = authenticationState.passwordRequirements,
                enableAuthentication = authenticationState.isFormValid(),
                onToggleMode = {
                    handleEvent(
                        AuthenticationEvent.ToggleAuthenticationMode)
                },
                first_name = authenticationState.first_name,
                onFirstNameChanged = {
                    handleEvent(AuthenticationEvent.FirstNameChanged(it))
                },
                last_name = authenticationState.last_name,
                onLastNameChanged = {
                    handleEvent(AuthenticationEvent.LastNameChanged(it))
                },
                error = authenticationState.error
            )
        }
    }
}
