package com.tajir.taskly.data.api.models

data class GeneralResponseTasks(
    val data: List<Task?>,
    val message: String,
    val success: Boolean
)
