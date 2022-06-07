package com.tajir.taskly.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tajir.taskly.data.models.UserState
import com.tajir.taskly.events.AuthenticationEvent
import com.tajir.taskly.R
import com.tajir.taskly.viewModels.AuthState
import com.tajir.taskly.viewModels.UserState

@Composable
fun MainScreen(
    navController: NavController
) {
    val authState = AuthState.current
    val userState = UserState.current
    val user : UserState = userState.userState.collectAsState().value

    // if not logged in and data is not being fetched, then go back to sign screen.
    if(!user.loading and !user.isLoggedIn()) {
        authState.handleEvent(
            AuthenticationEvent.SetError(error = stringResource(id = R.string.something_wrong_login))
        )
        navController.navigate("login_screen")
    }

    // wait for user to load
    if(user.loading) {
        CircularProgressIndicator()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = String.format("Welcome, %s", user.first_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = String.format("Name: %s %s", user.first_name, user.last_name),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = String.format("Email: %s", user.email),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
