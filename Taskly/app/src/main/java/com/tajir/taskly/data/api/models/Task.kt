package com.tajir.taskly.data.api.models

import java.time.LocalDateTime

data class Task(
    var completed_at: String? = null,
    val created_at: String,
    val description: String,
    val due_by: String,
    val id: String,
    val status: String,
    val title: String,
    val user_id: String
)

data class TaskCreate(
    val title: String,
    val description: String,
    val due_by: String
)

data class TaskUpdate(
    val title: String,
    val description: String,
    val due_by: String,
    val status: String
)

data class TaskParsed(
    var completed_at: LocalDateTime? = null,
    val created_at: LocalDateTime,
    var description: String,
    var due_by: LocalDateTime,
    var id: String,
    var status: String,
    var title: String,
    val user_id: String
)
