package com.tajir.taskly.viewModels

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }
