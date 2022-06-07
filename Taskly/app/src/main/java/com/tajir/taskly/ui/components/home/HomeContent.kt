package com.tajir.taskly.ui.components.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    tasks: List<Task?>,
    handleEvent: (event : TaskEvent) -> Unit,
    navController: NavController,
    taskParse: (task : Task) -> TaskParsed
) {
    Column {
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
