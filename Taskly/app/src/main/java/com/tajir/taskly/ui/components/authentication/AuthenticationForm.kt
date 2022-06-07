package com.tajir.taskly.ui.components.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tajir.taskly.data.stateModels.AuthenticationMode
import com.tajir.taskly.data.stateModels.PasswordRequirement
import com.tajir.taskly.R

@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    first_name: String?,
    onFirstNameChanged: (firstName : String) -> Unit,
    last_name: String?,
    onLastNameChanged: (lastName : String) -> Unit,
    email: String?,
    onEmailChanged: (email: String) -> Unit,
    password: String?,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    enableAuthentication: Boolean,
    completedPasswordRequirements: List<PasswordRequirement>,
    onToggleMode: () -> Unit,
    error: String?
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationTitle(authenticationMode = authenticationMode)
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = authenticationMode ==
                            AuthenticationMode.SIGN_UP
                ) {
                    Column {
                        NameInput(
                            name = first_name,
                            onNameChanged = onFirstNameChanged,
                            label_res = R.string.label_first_name
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NameInput(
                            name = last_name,
                            onNameChanged = onLastNameChanged,
                            label_res = R.string.label_last_name
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                EmailInput(
                    modifier = Modifier.fillMaxWidth(),
                    email = email ?: "",
                    onEmailChanged = onEmailChanged
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(
                    modifier = Modifier.fillMaxWidth(),
                    password = password ?: "",
                    onPasswordChanged = onPasswordChanged
                )
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedVisibility(
                    visible = authenticationMode ==
                            AuthenticationMode.SIGN_UP
                ) {
                    PasswordRequirements(
                        modifier = Modifier.fillMaxWidth(),
                        satisfiedRequirements = completedPasswordRequirements
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (error != null) {
                    if (error.isNotBlank() && error.isNotEmpty()) {
                        Text(
                            text = error,
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AuthenticationButton(
                    enableAuthentication = enableAuthentication,
                    authenticationMode = authenticationMode,
                    onAuthenticate = onAuthenticate
                )

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        ToggleAuthenticationMode(
            modifier = Modifier.fillMaxWidth(),
            authenticationMode = authenticationMode,
            toggleAuthentication = {
                onToggleMode()
            }
        )
    }
}
