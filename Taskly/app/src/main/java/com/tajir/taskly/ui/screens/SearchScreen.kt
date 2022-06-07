package com.tajir.taskly.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.ui.components.TaskList
import com.tajir.taskly.viewModels.TaskState
import com.tajir.taskly.viewModels.UserState
import kotlinx.coroutines.launch

fun searchFunc(s: String, tasks: List<Task>): List<Task> {
    val listA: MutableList<Task> = mutableListOf()
    val listB: MutableList<Task> = mutableListOf()
    val listC: MutableList<Task> = mutableListOf()

    val sl = s.lowercase()

    for (task in tasks) {
        val title = task.title.lowercase()
        val desc = task.description.lowercase()

        if (title.contains(sl) && desc.contains(sl)) {
            listA.add(task)
        } else if (title.contains(sl)) {
            listB.add(task)
        } else if (desc.contains(sl)) {
            listC.add(task)
        }
    }

    listA.addAll(listB)
    listA.addAll(listC)

    return listA
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    navController: NavController
) {
    val userState = UserState.current
    val taskState = TaskState.current
    val user: UserState = userState.userState.collectAsState().value
    val task: TaskState = taskState.taskState.collectAsState().value

    var search by remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Go Back", Modifier.padding(end = 4.dp))
                    }

                    OutlinedTextField(
                        value = search,
                        onValueChange = { s ->
                            search = s
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp),
                        label = {
                            Text(text = "Search")
                        }
                    )
                }
            },
            content = { padding ->
                Box(modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight()) {
                    TaskList(
                        tasks = searchFunc(search, task.tasks as List<Task>),
                        handleEvent = taskState::handleEvent,
                        navController = navController
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
