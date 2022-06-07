package com.tajir.taskly.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.UserEvent
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.ui.components.profile.ProfileContent
import com.tajir.taskly.viewModels.UserState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileScreen(
    navController: NavController
) {
    var isEdit by remember {
        mutableStateOf(false)
    }
    val userState = UserState.current
    val user: UserState = userState.userState.collectAsState().value

    val onEditChanged = {
        isEdit = !isEdit
    }

    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    // show apiMsg in SnackBar
    if (user.msg != null) {
        snackBarCoroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = user.msg,
                actionLabel = "Hide",
                duration = SnackbarDuration.Short
            )
        }
        userState.handleEvent(
            UserEvent.DismissMsg
        )
    }

    // wait for user and data to load
    if (user.loading || !user.isLoggedIn()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val navBackground = Color(0xFF1976D2)
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x009FB7))
                ) {
                    Text(
                        text = "Profile",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            },
            content = { padding ->
                Box(
                    modifier = Modifier.padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileContent(
                        isEdit = isEdit,
                        onEditChanged = onEditChanged,
                        handleEvent = userState::handleEvent,
                        user = user,
                        navController = navController,
                        isUpdateValid = user.isUserUpdateValid()
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    modifier = Modifier,
                    navBackground = navBackground
                )
            },
            scaffoldState = scaffoldState,
            backgroundColor = Color(0x009FB7)
        )
    }
}
