package com.tajir.taskly.ui.components.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tajir.taskly.events.TaskEvent

@Composable
fun TaskTopBar(
    isEdit: Boolean,
    editChanged: () -> Unit,
    onClose: () -> Unit,
    handleEvent: (event : TaskEvent) -> Unit,
    isUpdateEnabled: Boolean
){
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onClose
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
            TextButton(
                onClick = {
                    editChanged()
                    if(isEdit) {
                        handleEvent(
                            TaskEvent.UpdateTaskCall
                        )
                    }
                },
                enabled = isUpdateEnabled
            ) {
                Text(if(isEdit) "Update" else "edit")
            }
        }
    }
}
