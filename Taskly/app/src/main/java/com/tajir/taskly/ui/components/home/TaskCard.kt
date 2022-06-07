package com.tajir.taskly.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.events.TaskEvent

fun take_certain_chars(n : Int, str : String) : String {
    if(str.length <= n) {
        return str
    } else {
        return str.take(n) + "..."
    }
}

@Composable
fun TaskCard(
    task: Task,
    handleEvent: (event : TaskEvent) -> Unit,
    modifier: Modifier,
    onTaskEdit: () -> Unit,
    onTaskView: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Row(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clickable {
                    onTaskView()
                },
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        handleEvent(
                            TaskEvent.SelectTaskByID(task.id)
                        )
                        val newStatus = if(task.status=="pending") "completed" else "pending"
                        handleEvent(
                            TaskEvent.SetTaskStatus(newStatus)
                        )
                        handleEvent(
                            TaskEvent.UpdateTaskCall
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PanoramaFishEye,
                        contentDescription = "Status",
                        tint = if(task.status=="pending") Color.Red else Color.Green
                    )
                }

                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    textDecoration = if(task.status=="pending") TextDecoration.None else TextDecoration.LineThrough
                                )
                            ) {
                                append(task.title)
                            }
                        }
                    )
                }

                Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopEnd)) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = onTaskView) {
                            Text("View")
                        }
                        DropdownMenuItem(onClick = onTaskEdit) {
                            Text("Edit")
                        }
                        DropdownMenuItem(onClick = {
                            showDeleteConfirm = true
                        }) {
                            Text("Delete", color = Color.Red)
                        }
                    }
                }

                if (showDeleteConfirm) {
                    ConfirmDialog(
                        content = String.format("Delete task \"%s\"?", task.title),
                        onDismiss = { showDeleteConfirm = false },
                        onConfirm = {
                            showDeleteConfirm = false
                            // delete the task
                            handleEvent(
                                TaskEvent.DeleteTaskByID(id=task.id)
                            )
                            expanded = false
                        }
                    )
                }


            }
        }
    }
}