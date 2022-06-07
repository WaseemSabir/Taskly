package com.tajir.taskly.ui.components.insight

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.api.models.TaskParsed
import com.tajir.taskly.ui.components.home.dueToday
import com.tajir.taskly.viewModels.TaskState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun filterTasks(tks: List<Task>, f: String, parseTask : (task : Task)-> TaskParsed): List<Task?> {
    return when (f) {
        "Daily" -> {
            dueToday(tks, parseTask)
        }
        "Weekly" -> {
            val woy : TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            tks.filter {
                parseTask(it).due_by.toLocalDate().get(woy) == LocalDate.now().get(woy)
            }
        }
        "Monthly" -> {
            tks.filter {
                parseTask(it).due_by.monthValue == LocalDateTime.now().monthValue
            }
        }
        else -> {
            tks
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightContent(
    tks: List<Task>
) {
    val taskModel = TaskState.current
    val parseTask = {task : Task ->
        taskModel.parseTask(task)
    }
    var choice by remember {
        mutableStateOf("Weekly")
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    val choices = listOf("All", "Monthly", "Weekly", "Daily")

    Column {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    expanded = true
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Row {
                    Text(text = choice)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    choices.forEachIndexed { _, s ->
                        DropdownMenuItem(onClick = {
                            choice = s
                            expanded = false
                        }) {
                            Text(text = s)
                        }
                    }
                }
            }
        }

        val tasks = filterTasks(
            tks = tks,
            f = choice,
            parseTask = parseTask
        )

        InsightCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), heading = "Total Tasks", subtext = tasks.size.toString()
        )

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            InsightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                heading = "Completed",
                subtext = tasks.filter { (it?.status ?: "") == "completed" }.size.toString()
            )

            InsightCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                heading = "Pending",
                subtext = tasks.filter { (it?.status ?: "") == "pending" }.size.toString()
            )
        }
    }
}
