package com.tajir.taskly.data.api.models

data class GeneralResponseUser(
    val data: List<User?>,
    val message: String,
    val success: Boolean
)
