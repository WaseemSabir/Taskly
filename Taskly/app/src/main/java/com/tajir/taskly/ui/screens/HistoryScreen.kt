package com.tajir.taskly.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.api.models.TaskParsed
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.navigation.NavigationItem
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.ui.components.TaskList
import com.tajir.taskly.ui.components.home.GenericTopBar
import com.tajir.taskly.viewModels.TaskState
import com.tajir.taskly.viewModels.UserState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun dueYesterday(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS)
    val filteredTks = tks.filter {tk ->
        tk?.let { parseTask(it).due_by.toLocalDate() } == yesterday
    }
    return filteredTks
}

@RequiresApi(Build.VERSION_CODES.O)
fun duePastWeek(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS)
    val pastWeek = LocalDate.now().minus(1, ChronoUnit.WEEKS)

    val filteredTks = tks.filter {tk ->
        yesterday > tk?.let { parseTask(it).due_by.toLocalDate() } &&
                tk?.let { parseTask(it).due_by.toLocalDate() }!! > pastWeek
    }
    return filteredTks
}

@RequiresApi(Build.VERSION_CODES.O)
fun dueInPast(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val pastWeek = LocalDate.now().minus(1, ChronoUnit.WEEKS)

    val filteredTks = tks.filter {tk ->
        tk?.let { parseTask(it).due_by.toLocalDate() }!! < pastWeek
    }
    return filteredTks
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    navController: NavController
) {
    val userState = UserState.current
    val taskState = TaskState.current
    val user: UserState = userState.userState.collectAsState().value
    val task: TaskState = taskState.taskState.collectAsState().value

    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    val parseTask = {task : Task ->
        taskState.parseTask(task)
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
                    title = "History"
                )
            },
            content = { padding ->
                Box(modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight()) {
                    Column {
                        TaskList(
                            tasks = dueYesterday(task.tasks, parseTask),
                            handleEvent = taskState::handleEvent,
                            navController = navController,
                            labelText = "Due Yesterday"
                        )

                        TaskList(
                            tasks = duePastWeek(task.tasks, parseTask),
                            handleEvent = taskState::handleEvent,
                            navController = navController,
                            labelText = "Due Past Week"
                        )

                        TaskList(
                            tasks = dueInPast(task.tasks, parseTask),
                            handleEvent = taskState::handleEvent,
                            navController = navController,
                            labelText = "Due In Past"
                        )
                    }
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
