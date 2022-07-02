package com.tajir.taskly.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.AuthenticationEvent
import com.tajir.taskly.R
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.viewModels.AuthState
import com.tajir.taskly.viewModels.TaskState
import com.tajir.taskly.viewModels.UserState
import kotlinx.coroutines.launch
import com.tajir.taskly.navigation.NavigationItem.Search
import com.tajir.taskly.ui.components.home.*

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    navController: NavController
) {
    val authState = AuthState.current
    val userState = UserState.current
    val taskState = TaskState.current
    val user: UserState = userState.userState.collectAsState().value
    val task: TaskState = taskState.taskState.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    var showDialog by remember {
        mutableStateOf(false)
    }

    // if not logged in and data is not being fetched, then go back to sign screen.
    if (!user.loading and !user.isLoggedIn()) {
        authState.handleEvent(
            AuthenticationEvent.SetError(error = stringResource(id = R.string.something_wrong_login))
        )
        navController.navigate("login_screen")
    }

    // show apiMsg in SnackBar
    if (task.msg != null && task.msg.isNotEmpty()) {
        snackBarCoroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = task.msg,
                actionLabel = "Hide",
                duration = SnackbarDuration.Short
            )
        }
        taskState.handleEvent(
            TaskEvent.DismissMsg
        )
    }

    // shows a dialog to create new task
    AnimatedVisibility(
        visible = showDialog,
        enter = fadeIn(animationSpec = tween(1000)) +
                expandVertically(
                    animationSpec = tween(1500)
                ),
        exit = fadeOut(animationSpec = tween(1000)) +
                shrinkVertically(
                    animationSpec = tween(1500)
                )
    ) {
        TaskCreateDialog(
            showDialog = showDialog,
            onClose = { showDialog = false },
            handleEvent = taskState::handleEvent,
            taskState = taskState.taskState.collectAsState().value,
            enableCreation = taskState.taskState.collectAsState().value.isSelectedTaskCreateValid()
        )
    }

    // wait for user and data to load
    if (user.loading || task.loading) {
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
                GenericTopBar(
                    onSearchClick = {
                        navController.navigate(Search.route)
                    },
                    onRefreshClick = {
                        taskState.handleEvent(
                            TaskEvent.RefreshTasks
                        )
                    },
                    onLogoutClick = {
                        navController.navigate("login_screen")
                    },
                    backgroundColor = Color(0x009FB7),
                    title = "My Tasks"
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingButtonHome(
                    onClick = {
                        taskState.handleEvent(
                            TaskEvent.ResetCreationArgs
                        )
                        showDialog = true
                    }
                )
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    HomeContent(
                        tks = task.tasks,
                        handleEvent = taskState::handleEvent,
                        navController = navController,
                        taskParse = taskState::parseTask
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
