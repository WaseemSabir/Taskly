package com.tajir.taskly.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tajir.taskly.viewModels.AuthenticationViewModel
import com.tajir.taskly.ui.components.AuthenticationComponent
import com.tajir.taskly.ui.theme.TasklyTheme

@Composable
fun AuthenticationScreen() {
    val viewModel: AuthenticationViewModel = viewModel()

    TasklyTheme {
        AuthenticationComponent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState =
            viewModel.uiState.collectAsState().value,
            handleEvent = viewModel::handleEvent
        )
    }
}