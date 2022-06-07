package com.tajir.taskly.data.api.models

data class GeneralResponseAuth(
    val data: List<Token?>,
    val message: String,
    val success: Boolean
)
