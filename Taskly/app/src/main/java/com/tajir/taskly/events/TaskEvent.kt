package com.tajir.taskly.events

import java.time.LocalDateTime

sealed class TaskEvent{

    class TokenChanged(val token: String): TaskEvent()

    class SetTaskTitle(val title: String): TaskEvent()

    class SetTaskDesc(val desc: String): TaskEvent()

    class SetTaskDueDate(val due: LocalDateTime): TaskEvent()

    class SetTaskStatus(val status: String): TaskEvent()

    class SelectTaskByID(val id: String): TaskEvent()

    object CreateTaskCall: TaskEvent()

    object UpdateTaskCall: TaskEvent()

    class DeleteTaskByID(val id: String): TaskEvent()

    class SetMsg(val msg : String): TaskEvent()

    object DismissMsg: TaskEvent()

    object RefreshTasks: TaskEvent()

    object Logout: TaskEvent()

    object ResetCreationArgs: TaskEvent()
}
