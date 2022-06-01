package com.tajir.taskly.ui.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.AlertDialog
import androidx.compose.ui.layout.layout
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.events.TaskEvent
import java.time.LocalDateTime
import java.time.LocalTime


enum class CustomDialogPosition {
    BOTTOM, TOP
}

fun Modifier.customDialogModifier(pos: CustomDialogPosition) = layout { measurable, constraints ->

    val placeable = measurable.measure(constraints);
    layout(constraints.maxWidth, constraints.maxHeight){
        when(pos) {
            CustomDialogPosition.BOTTOM -> {
                placeable.place(0, constraints.maxHeight - placeable.height, 10f)
            }
            CustomDialogPosition.TOP -> {
                placeable.place(0,0,10f)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TaskCreateDialog(
    showDialog: Boolean,
    onClose: () -> Unit,
    handleEvent: (event : TaskEvent) -> Unit,
    taskState: TaskState,
    enableCreation: Boolean
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onClose,
            title = {
                Text(text = "Add new Task")
            },
            text = {
                   CreateTaskForm(
                       title = taskState.title ?: "",
                       titleChanged = {
                           handleEvent(TaskEvent.SetTaskTitle(it))
                       },
                       description = taskState.desc ?: "",
                       descriptionChanged = {
                           handleEvent(TaskEvent.SetTaskDesc(it))
                       },
                       dueDate = taskState.dueDate ?: LocalDateTime.now().with(LocalTime.MAX),
                       dueDateChanged = {
                           handleEvent(TaskEvent.SetTaskDueDate(it))
                       },
                       modifier = Modifier
                   )
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        handleEvent(
                            TaskEvent.CreateTaskCall
                        )
                        onClose()
                    },
                    enabled = enableCreation
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onClose
                ) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.customDialogModifier(CustomDialogPosition.TOP)
        )
    }
}
