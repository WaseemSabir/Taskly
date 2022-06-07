package com.tajir.taskly.data.stateModels

import com.tajir.taskly.data.api.models.Task
import com.tajir.taskly.data.api.models.TaskParsed
import java.time.LocalDateTime

data class TaskState constructor(
    val tasks: List<Task?> = emptyList(),
    val loading: Boolean = false,
    val token: String? = null,
    val selectedTask: TaskParsed? = null,
    val title: String? = null,
    val desc: String? = null,
    val status: String? = null,
    val dueDate: LocalDateTime? = null,
    val msg: String? = null
) {
    fun isSelectedTaskCreateValid() : Boolean {
        if(title != null && desc != null) {
            return title.isNotEmpty() && desc.isNotEmpty()
        }

        return false
    }

    fun isSelectedTaskUpdateValid() : Boolean {
        if(title != null && desc != null && status != null) {
            return title.isNotEmpty() && desc.isNotEmpty() &&
                    (status=="pending" || status=="completed")
        }

        return false
    }
}
