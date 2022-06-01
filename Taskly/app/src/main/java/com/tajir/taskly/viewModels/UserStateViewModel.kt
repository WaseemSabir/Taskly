package com.tajir.taskly.viewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tajir.taskly.data.api.models.User
import com.tajir.taskly.data.api.repository.UserRepository
import com.tajir.taskly.data.stateModels.UserState
import com.tajir.taskly.events.UserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class UserStateViewModel : ViewModel() {
    val userState = MutableStateFlow(UserState())

    private fun fetchUser() {
        val userRepo = UserRepository()
        setLoading(true)

        viewModelScope.launch {
            val res = userRepo.retrieve(token = userState.value.token ?: "")
            val body = res.body()

            if (body != null) {
                val data: User = body.data[0]!!
                userState.value = userState.value.copy(
                    first_name = data.first_name,
                    last_name = data.last_name,
                    email = data.email
                )
            }
            setLoading(false)
        }
    }

    private fun updateToken(token: String?) {
        userState.value = userState.value.copy(
            token = token
        )
    }

    private fun setLoading(loading: Boolean) {
        userState.value = userState.value.copy(
            loading = loading
        )
    }

    private fun clearState() {
        userState.value = userState.value.copy(
            first_name = null,
            last_name = null,
            email = null,
            token = null,
            loading = false
        )
    }

    fun handleEvent(userEvent: UserEvent) {
        when (userEvent) {
            is UserEvent.TokenChanged -> {
                updateToken(userEvent.token)
                fetchUser()
            }
            is UserEvent.Logout -> {
                clearState()
            }
        }
    }
}

val UserState by lazy { compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") } }
