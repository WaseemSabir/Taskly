package com.tajir.taskly.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.home.TaskCard

@Composable
fun TaskList(
    tasks: List<Task?>,
    handleEvent: (event: TaskEvent) -> Unit,
    navController: NavController,
    labelText: String? = null,
    modifier: Modifier = Modifier.padding(vertical = 8.dp)
) {

    Column(modifier = modifier) {
        if(labelText!=null) {
            Text(
                text = labelText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color.DarkGray
            )
        }
        if (tasks.isEmpty()) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "No Task Found.",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        } else {
            LazyColumn {
                items(tasks) { task ->
                    if (task != null) {
                        TaskCard(
                            task = task,
                            handleEvent = handleEvent,
                            modifier = Modifier.animateContentSize(),
                            onTaskEdit = {
                                handleEvent(
                                    TaskEvent.SelectTaskByID(task.id)
                                )
                                navController.navigate("task_edit")
                            },
                            onTaskView = {
                                handleEvent(
                                    TaskEvent.SelectTaskByID(task.id)
                                )
                                navController.navigate("task_view")
                            }
                        )
                    }
                }
            }
        }
    }
}