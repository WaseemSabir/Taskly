package com.tajir.taskly.ui.components.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.home.OutlinedInputField
import com.tajir.taskly.viewModels.TaskState
import com.tajir.taskly.ui.components.home.getStringFromDate
import com.tajir.taskly.ui.components.home.selectDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskContent(
    isEdit: Boolean,
    editChanged: () -> Unit,
    handleEvent: (event: TaskEvent) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    val taskModel = TaskState.current
    val currTask = taskModel.taskState.collectAsState().value.selectedTask
    val title = taskModel.taskState.collectAsState().value.title
    val desc = taskModel.taskState.collectAsState().value.desc
    val status = taskModel.taskState.collectAsState().value.status

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        val verticalPad = modifier.padding(vertical = 6.dp)
        Column(modifier = modifier) {

            // title
            if (!isEdit) {
                Text(
                    text = title ?: "",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = verticalPad
                )
            } else {
                OutlinedInputField(
                    value = title ?: "",
                    modifier = verticalPad,
                    label = "Title",
                    onChangedHandler = { title ->
                        handleEvent(
                            TaskEvent.SetTaskTitle(title)
                        )
                    }
                )
            }

            // description
            if (!isEdit) {
                Text(
                    text = desc ?: "",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                    modifier = verticalPad
                )
            } else {
                OutlinedInputField(
                    value = desc ?: "",
                    modifier = verticalPad,
                    label = "Description",
                    onChangedHandler = { desc ->
                        handleEvent(
                            TaskEvent.SetTaskDesc(desc)
                        )
                    }
                )
            }

            // Created at
            Text(
                text = String.format("Created on %s", getStringFromDate(currTask!!.created_at)),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Normal,
                modifier = verticalPad
            )

            // Completed at
            if(status=="completed" && currTask.completed_at != null) {
                Text(
                    text = String.format("Completed on %s", getStringFromDate(currTask.completed_at!!)),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Normal,
                    modifier = verticalPad
                )
            }

            // Due Date
            Button(
                onClick = {
                    selectDateTime(context, currTask!!.due_by) {
                        handleEvent(
                            TaskEvent.SetTaskDueDate(it)
                        )
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF55B5F1),
                    disabledBackgroundColor = Color(0xFF55c2da),
                    disabledContentColor = Color.Black
                ),
                enabled = isEdit,
                modifier = verticalPad
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Picker",
                    Modifier.padding(end = 8.dp)
                )
                Text(text = getStringFromDate(currTask!!.due_by))
            }

            // Status
            Box(modifier = modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        expanded = true
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF55B5F1),
                        disabledBackgroundColor = Color(0xFF55c2da),
                        disabledContentColor = Color.Black
                    ),
                    enabled = isEdit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (status == "pending") Icons.Default.Pending else Icons.Default.Check,
                        contentDescription = "Status",
                        Modifier.padding(end = 8.dp)
                    )
                    Text(text = status ?: "")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(onClick = {
                        handleEvent(
                            TaskEvent.SetTaskStatus("pending")
                        )
                        expanded = false
                    }) {
                        Text(text = "Pending")
                    }

                    DropdownMenuItem(onClick = {
                        handleEvent(
                            TaskEvent.SetTaskStatus("completed")
                        )
                        expanded = false
                    }) {
                        Text(text = "Completed")
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                         handleEvent(
                             TaskEvent.DeleteTaskByID(currTask.id)
                         )
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Red
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                    modifier = Modifier.padding(end=3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }

                if(isEdit) {
                    Button(
                        onClick = {
                            handleEvent(
                                TaskEvent.UpdateTaskCall
                            )
                            editChanged()
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Update"
                        )
                    }
                } else {
                    Button(
                        onClick = editChanged,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            }


        }
    }

}