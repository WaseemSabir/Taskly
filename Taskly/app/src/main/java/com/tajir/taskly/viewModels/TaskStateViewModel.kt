package com.tajir.taskly.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajir.taskly.data.api.models.*
import com.tajir.taskly.data.api.repository.TaskRepository
import com.tajir.taskly.data.stateModels.TaskState
import com.tajir.taskly.events.TaskEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.Exception


class TaskStateViewModel : ViewModel() {
    val taskState = MutableStateFlow(TaskState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectByID(id: String) {
        var found = false
        for (task in taskState.value.tasks) {
            if (task?.id == id) {
                val t = parseTask(task)
                setSelectedTask(task = t)
                found = true
                break
            }
        }
        if (!found) {
            setSelectedTask(task = null)
        }
    }

    private fun fetchTasks() {

        if (taskState.value.token == null) {
            return
        }

        val taskRepo = TaskRepository()
        setLoading(true)

        viewModelScope.launch {
            val res = taskRepo.retrieve_all(token = taskState.value.token ?: "")
            val body: GeneralResponseTasks? = res.body()

            if (body != null) {
                val data: List<Task?> = body.data ?: emptyList()
                taskState.value = taskState.value.copy(
                    tasks = data
                )
            }
            setLoading(false)
        }
    }

    private fun updateToken(token: String?) {
        taskState.value = taskState.value.copy(
            token = token
        )
    }

    private fun setLoading(loading: Boolean) {
        taskState.value = taskState.value.copy(
            loading = loading
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSelectedTask(task: TaskParsed?) {
        taskState.value.copy(
            selectedTask = task,
            title = task?.title ?: "",
            desc = task?.description ?: "",
            dueDate = task?.due_by ?: LocalDateTime.now().with(LocalTime.MAX),
            status = task?.status ?: "pending"
        ).also { taskState.value = it }
    }

    private fun setTaskTitle(title: String) {
        taskState.value = taskState.value.copy(
            title = title
        )
    }

    private fun setTaskDesc(desc: String) {
        taskState.value = taskState.value.copy(
            desc = desc
        )
    }

    private fun setTaskDueDate(due: LocalDateTime) {
        taskState.value = taskState.value.copy(
            dueDate = due
        )
    }

    private fun setTaskStatus(status: String) {
        taskState.value = taskState.value.copy(
            status = status
        )
    }

    private fun setMsg(msg: String) {
        taskState.value = taskState.value.copy(
            msg = msg
        )
    }

    private fun dismissMsg() {
        taskState.value = taskState.value.copy(
            msg = null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDatetime(dt: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dt.format(formatter)
    }

    private fun appendOrReplaceFromList(task: Task?) {
        val tasks = taskState.value.tasks.toMutableList()
        var found = false

        if (task != null) {

            if(tasks.size>0) {
                for (i in 0 until tasks.size) {
                    val t = tasks[i]
                    if (t?.id == task.id) {
                        tasks[i] = task
                        found = true
                        break
                    }
                }
            }

            if (!found) {
                tasks.add(task)
            }

            taskState.value = taskState.value.copy(
                tasks = tasks
            )
        }
    }

    private fun removeFromList(id: String) {
        val tasks = taskState.value.tasks.toMutableList()
        var index: Int? = null

        if(tasks.size>0) {
            for (i in 0 until tasks.size) {
                val t = tasks[i]
                if (t?.id == id) {
                    index = i
                    break
                }
            }
        }

        if (index != null) {
            tasks.removeAt(index)
        }

        taskState.value = taskState.value.copy(
            tasks = tasks
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTaskCallHandler() {
        setLoading(true)
        if (taskState.value.isSelectedTaskCreateValid()) {
            val toCreate = TaskCreate(
                title = taskState.value.title ?: "",
                description = taskState.value.desc ?: "",
                due_by = formatDatetime(taskState.value.dueDate ?: LocalDateTime.now().with(LocalTime.MAX))
            )

            val taskRepo = TaskRepository()
            viewModelScope.launch {
                try {
                    val res = taskRepo.new(token = taskState.value.token ?: "", data = toCreate)
                    val body = res.body()

                    if (body == null || body.data.isEmpty()) {
                        setMsg("Task creation failed.")
                    } else {
                        val task: Task? = body.data[0]
                        appendOrReplaceFromList(task)
                        setMsg("Task Created.")
                        selectByID(task!!.id)
                    }
                } catch (e: Exception) {
                    println(e)
                    setMsg("Check Your Internet Connection.")
                }
            }

        } else {
            setMsg("All fields are required.")
        }
        setLoading(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskCallHandle() {
        setLoading(true)
        if (taskState.value.isSelectedTaskUpdateValid()) {
            val toUpdate = TaskUpdate(
                title = taskState.value.title ?: "",
                description = taskState.value.desc ?: "",
                due_by = formatDatetime(taskState.value.dueDate ?: LocalDateTime.now().with(LocalTime.MAX)),
                status = taskState.value.status ?: ""
            )

            val taskRepo = TaskRepository()
            viewModelScope.launch {
                try {
                    val res = taskRepo.edit(
                        token = taskState.value.token ?: "",
                        data = toUpdate,
                        tid = taskState.value.selectedTask!!.id
                    )

                    val body = res.body()
                    if (body == null || body.data.isEmpty()) {
                        setMsg("Task Update failed.")
                    } else {
                        val task: Task? = body.data[0]
                        appendOrReplaceFromList(task)
                        setMsg("Task Updated.")
                        selectByID(task!!.id)
                    }

                } catch (e: Exception) {
                    setMsg("Check Your Internet Connection.")
                }
            }
        } else {
            setMsg("fields are required.")
        }
        setLoading(false)
    }

    private fun deleteTaskCallHandler(id: String) {
        setLoading(true)
        val taskRepo = TaskRepository()

        viewModelScope.launch {
            try {
                val res = taskRepo.remove(token = taskState.value.token ?: "", tid = id)
                if (res.code() == 200) {
                    removeFromList(id)
                    setMsg("Task deleted successfully.")
                } else {
                    setMsg("Task deletion failed.")
                }
            } catch (e: Exception) {
                setMsg("Check Your Internet Connection.")
            }
        }
        setLoading(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetCreationArgs() {
        taskState.value = taskState.value.copy(
            title = "",
            desc = "",
            dueDate = LocalDateTime.now().with(LocalTime.MAX)
        )
    }

    private fun clearState() {
        taskState.value = taskState.value.copy(
            tasks = emptyList(),
            loading = false,
            token = null,
            selectedTask = null,
            title = null,
            desc = null,
            status = null,
            dueDate = null,
            msg = null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleEvent(taskEvent: TaskEvent) {
        when (taskEvent) {
            is TaskEvent.TokenChanged -> {
                updateToken(taskEvent.token)
                fetchTasks()
            }
            is TaskEvent.SetTaskDueDate -> {
                setTaskDueDate(taskEvent.due)
            }
            is TaskEvent.SetTaskTitle -> {
                setTaskTitle(taskEvent.title)
            }
            is TaskEvent.SetTaskDesc -> {
                setTaskDesc(taskEvent.desc)
            }
            is TaskEvent.SetTaskStatus -> {
                setTaskStatus(taskEvent.status)
            }
            is TaskEvent.SelectTaskByID -> {
                selectByID(taskEvent.id)
            }
            is TaskEvent.CreateTaskCall -> {
                createTaskCallHandler()
            }
            is TaskEvent.UpdateTaskCall -> {
                updateTaskCallHandle()
            }
            is TaskEvent.DeleteTaskByID -> {
                deleteTaskCallHandler(taskEvent.id)
            }
            is TaskEvent.DismissMsg -> {
                dismissMsg()
            }
            is TaskEvent.SetMsg -> {
                setMsg(taskEvent.msg)
            }
            is TaskEvent.RefreshTasks -> {
                fetchTasks()
            }
            is TaskEvent.Logout -> {
                clearState()
            }
            is TaskEvent.ResetCreationArgs -> {
                resetCreationArgs()
            }
        }
    }

    private fun formatStringDT(dt: String): String {
        val parts = dt.split(" ").toMutableList()
        parts.removeAt(0)
        parts.removeLast()
        return parts.joinToString(separator = " ")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseTask(task: Task): TaskParsed {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        var compAt: LocalDateTime? = null
        if (task.completed_at != null) {
            compAt = LocalDateTime.parse(formatStringDT(task.completed_at!!), formatter)
        }

        return TaskParsed(
            completed_at = compAt,
            created_at = LocalDateTime.parse(formatStringDT(task.created_at), formatter),
            description = task.description,
            due_by = LocalDateTime.parse(formatStringDT(task.due_by), formatter),
            id = task.id,
            status = task.status,
            title = task.title,
            user_id = task.user_id
        )
    }
}

val TaskState by lazy { compositionLocalOf<TaskStateViewModel> { error("User State Context Not Found!") } }
