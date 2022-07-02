package com.tajir.taskly.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tajir.taskly.data.api.models.GeneralResponseAuth
import com.tajir.taskly.data.stateModels.AuthenticationMode
import com.tajir.taskly.events.AuthenticationEvent
import com.tajir.taskly.events.UserEvent
import com.tajir.taskly.ui.components.authentication.AuthenticationComponent
import com.tajir.taskly.ui.theme.TasklyTheme
import com.tajir.taskly.viewModels.AuthState
import com.tajir.taskly.viewModels.UserState
import com.tajir.taskly.R
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.viewModels.TaskState

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun AuthenticationScreen(navController: NavController) {
    val authModel = AuthState.current
    val userModel = UserState.current
    val taskModel = TaskState.current
    val currAuthState = authModel.uiState.collectAsState().value
    val authResponse = currAuthState.authResponse

    // checks if reposne contains a token, if it does, it navigates it to main screen
    // else it will set error and clear the auth response object
    if(authResponse != null) {
        val resBody : GeneralResponseAuth? = authResponse.body()
        if(resBody == null) {
            var error: String;
            if(currAuthState.authenticationMode == AuthenticationMode.SIGN_IN) {
                error = stringResource(id = R.string.login_err)
            } else{
                error = stringResource(id = R.string.email_exist_err)
            }
            authModel.handleEvent(
                AuthenticationEvent.SetError(error = error)
            )
        } else {
            val token = resBody.data[0]?.token
            userModel.handleEvent(
                UserEvent.TokenChanged(token = token ?: "")
            )
            taskModel.handleEvent(
                TaskEvent.TokenChanged(token = token ?: "")
            )
            navController.navigate("home_screen")
        }
        authModel.handleEvent(
            AuthenticationEvent.ClearAuthResponse
        )
    }

    TasklyTheme {
        AuthenticationComponent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState =
            authModel.uiState.collectAsState().value,
            handleEvent = authModel::handleEvent
        )
    }
}
