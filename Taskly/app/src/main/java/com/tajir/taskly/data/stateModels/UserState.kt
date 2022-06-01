package com.tajir.taskly.data.stateModels

data class UserState(
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val token: String? = null,
    val loading: Boolean = false,
    val msg: String? = null
) {
    fun isLoggedIn(): Boolean {
        if(email != null && token != null) {
            return email.isNotEmpty() && token.isNotEmpty()
        }
        return false
    }

    fun isUserUpdateValid(): Boolean {
        if(email != null && first_name != null && last_name != null && token != null) {
            return email.isNotEmpty() && first_name.isNotEmpty() && last_name.isNotEmpty() &&
                    token.isNotEmpty()
        }

        return false
    }
}
