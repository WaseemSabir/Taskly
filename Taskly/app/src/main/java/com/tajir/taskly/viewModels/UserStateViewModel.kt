package com.tajir.taskly.viewModels

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var email = mutableStateOf("")
    var password = mutableStateOf("")

    fun updatePassword(pass : String) {
        password.value = pass;
    }

    fun updateEmail(em : String) {
        email.value = em;
    }
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }
