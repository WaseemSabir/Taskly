package com.tajir.taskly.data.api.models

data class User(
    val first_name: String,
    val last_name: String,
    val email: String
)

data class UserWithPassword(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)
