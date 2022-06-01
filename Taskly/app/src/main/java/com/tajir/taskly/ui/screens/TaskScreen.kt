package com.tajir.taskly.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.BottomNavigationBar
import com.tajir.taskly.ui.components.tasks.TaskContent
import com.tajir.taskly.ui.components.tasks.TaskTopBar
import com.tajir.taskly.viewModels.TaskState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    type : String,
    navController : NavController
) {
    val taskModel = TaskState.current
    var isEdit by remember {
        mutableStateOf(type=="edit")
    }
    val editChanged = {
        isEdit = !isEdit
    }
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    val currTask = taskModel.taskState.collectAsState().value.selectedTask
    val title = taskModel.taskState.collectAsState().value.title
    val desc = taskModel.taskState.collectAsState().value.desc

    // show apiMsg in SnackBar
    val task: TaskState = taskModel.taskState.collectAsState().value
    if (task.msg != null && task.msg.isNotEmpty()) {
        snackBarCoroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = task.msg,
                actionLabel = "Hide",
                duration = SnackbarDuration.Short
            )
        }
        taskModel.handleEvent(
            TaskEvent.DismissMsg
        )
    }

    // wait for user and data to load
    if (title==null || desc==null || currTask==null) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Could not Show task.")
            Button(onClick = { navController.navigate("home_screen") }) {
                Text(text = "Go Back")
            }
        }
    } else {
        val navBackground = Color(0xFF1976D2)
        Scaffold(
            topBar = {
                TaskTopBar(
                    isEdit = isEdit,
                    editChanged = editChanged,
                    onClose = {
                        navController.popBackStack()
                    },
                    handleEvent = taskModel::handleEvent,
                    isUpdateEnabled = task.isSelectedTaskUpdateValid()
                )
            },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    TaskContent(
                        isEdit = isEdit,
                        editChanged = editChanged,
                        handleEvent = taskModel::handleEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 2.dp)
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
