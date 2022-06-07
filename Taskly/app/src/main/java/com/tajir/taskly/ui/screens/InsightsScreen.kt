package com.tajir.taskly.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.navigation.NavigationItem
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.ui.components.home.GenericTopBar
import com.tajir.taskly.ui.components.insight.InsightContent
import com.tajir.taskly.viewModels.TaskState
import com.tajir.taskly.viewModels.UserState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen(
    navController: NavController
) {
    val userState = UserState.current
    val taskState = TaskState.current
    val user: UserState = userState.userState.collectAsState().value
    val task: TaskState = taskState.taskState.collectAsState().value

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
                        navController.navigate(NavigationItem.Search.route)
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
                    title = "Insights"
                )
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    InsightContent(
                        tks = task.tasks as List<Task>
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
            backgroundColor = Color(0x009FB7)
        )
    }
}
