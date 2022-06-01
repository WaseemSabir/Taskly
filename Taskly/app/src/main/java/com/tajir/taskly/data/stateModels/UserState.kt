package com.tajir.taskly.data.stateModels

data class UserState(
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val token: String? = null,
    val loading: Boolean = false
) {
    fun isLoggedIn(): Boolean {
        if(email != null && token != null) {
            return email.isNotEmpty() && token.isNotEmpty()
        }
        return false
    }
}
