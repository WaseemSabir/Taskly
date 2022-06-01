package com.tajir.taskly.ui.components.home

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
import androidx.navigation.NavController
import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.api.models.TaskParsed
import com.tajir.taskly.events.TaskEvent
import com.tajir.taskly.ui.components.TaskList
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun dueToday(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val filteredTks = tks.filter {tk ->
            tk?.let { parseTask(it).due_by.toLocalDate() } == LocalDate.now()
    }
    return filteredTks
}

@RequiresApi(Build.VERSION_CODES.O)
fun dueThisWeek(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val today = LocalDate.now()
    val nextWeek = LocalDate.now().plus(1, ChronoUnit.WEEKS)

    val filteredTks = tks.filter {tk ->
        today < tk?.let { parseTask(it).due_by.toLocalDate() } &&
                tk?.let { parseTask(it).due_by.toLocalDate() }!! < nextWeek
    }
    println(nextWeek)
    return filteredTks
}

@RequiresApi(Build.VERSION_CODES.O)
fun dueAfterThisWeek(tks : List<Task?>, parseTask : (task : Task) -> TaskParsed) : List<Task?> {
    val nextWeek = LocalDate.now().plus(1, ChronoUnit.WEEKS)

    val filteredTks = tks.filter {tk ->
        tk?.let { parseTask(it).due_by.toLocalDate() }!! >= nextWeek
    }
    return filteredTks
}

fun applySort(tks : List<Task>, sortChoice : String, parseTask : (task : Task) -> TaskParsed) : List<Task> {
    if(sortChoice=="Created At") {
        return tks.sortedBy {
            parseTask(it).created_at
        }
    } else if(sortChoice=="Status") {
        val listA = mutableListOf<Task>()
        val listB = mutableListOf<Task>()

        for(task in tks) {
            if(task.status == "pending") {
                listA.add(task)
            } else (
                listB.add(task)
            )
        }
        listA.addAll(listB)
        return listA
    } else if(sortChoice=="Alphabetically") {
        return tks.sortedBy {
            it.title
        }
    }

    return tks.sortedBy {
        parseTask(it).due_by
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun applyFilter(tks : List<Task>, filterChoice : String, parseTask : (task : Task) -> TaskParsed) : List<Task> {
    if(filterChoice=="Pending Only") {
        return tks.filter {
            it.status == "pending"
        }
    } else if(filterChoice=="Completed Only") {
        return tks.filter {
            it.status == "completed"
        }
    } else if(filterChoice=="Created Today") {
        return tks.filter {
            parseTask(it).due_by.toLocalDate() == LocalDate.now()
        }
    }

    return tks
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    tks: List<Task?>,
    handleEvent: (event : TaskEvent) -> Unit,
    navController: NavController,
    taskParse: (task : Task) -> TaskParsed
) {

    var sortChoice by remember {
        mutableStateOf("Due Date")
    }
    val sortChoices = listOf("Due Date","Created At","Status","Alphabetically")
    var sortExpanded by remember {
        mutableStateOf(false)
    }

    var filterChoice by remember {
        mutableStateOf("Show All")
    }
    val filterChoices = listOf("Show All","Pending Only","Completed Only","Created Today")
    var filterExpanded by remember {
        mutableStateOf(false)
    }

    val tks1 = applySort(tks as List<Task>, sortChoice, taskParse)
    val tasks = applyFilter(tks1, filterChoice, taskParse)

    Column {

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    sortExpanded = true
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Row {
                    Text(text = sortChoice)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    expanded = sortExpanded,
                    onDismissRequest = { sortExpanded = false },
                ) {
                    sortChoices.forEachIndexed { _, s ->
                        DropdownMenuItem(onClick = {
                            sortChoice = s
                            sortExpanded = false
                        }) {
                            Text(text = s)
                        }
                    }
                }
            }

            TextButton(
                onClick = {
                    filterExpanded = true
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Row {
                    Text(text = filterChoice)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = { filterExpanded = false },
                ) {
                    filterChoices.forEachIndexed { _, s ->
                        DropdownMenuItem(onClick = {
                            filterChoice = s
                            filterExpanded = false
                        }) {
                            Text(text = s)
                        }
                    }
                }
            }
        }

        TaskList(
            tasks = dueToday(tasks, taskParse),
            handleEvent = handleEvent,
            navController = navController,
            labelText = "Due Today"
        )

        TaskList(
            tasks = dueThisWeek(tasks, taskParse),
            handleEvent = handleEvent,
            navController = navController,
            labelText = "Due This Week"
        )

        TaskList(
            tasks = dueAfterThisWeek(tasks, taskParse),
            handleEvent = handleEvent,
            navController = navController,
            labelText = "Due In Future"
        )
    }
}
