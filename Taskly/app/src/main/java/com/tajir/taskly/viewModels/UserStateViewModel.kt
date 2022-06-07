package com.tajir.taskly.viewModels

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

    private fun setFirstName(fname: String) {
        userState.value = userState.value.copy(
            first_name = fname
        )
    }

    private fun setLastName(lname: String) {
        userState.value = userState.value.copy(
            last_name = lname
        )
    }

    private fun setEmail(email: String) {
        userState.value = userState.value.copy(
            email = email
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

    private fun setMsg(msg: String) {
        userState.value = userState.value.copy(
            msg = msg
        )
    }

    private fun dismissMsg() {
        userState.value = userState.value.copy(
            msg = null
        )
    }

    private fun updateUserHandler() {
        val currUser = User(
            first_name = userState.value.first_name ?: "",
            last_name = userState.value.last_name ?: "",
            email = userState.value.email ?: ""
        )

        if (userState.value.isUserUpdateValid()) {
            val repo = UserRepository()

            viewModelScope.launch {
                try {
                    val res = repo.edit(data = currUser, token = userState.value.token ?: "")

                    val body = res.body()
                    if (body == null || body.data.isEmpty()) {
                        setMsg("User Update failed.")
                    } else {
                        setMsg("User Updated.")
                    }
                } catch (e: Exception) {
                    setMsg("Check Your Internet Connection.")
                }
            }
        } else {
            setMsg("All fields are requited.")
        }
    }

    fun userDeleteHandler() {
        val repo = UserRepository()

        viewModelScope.launch {
            try {
                val res = repo.remove(token = userState.value.token ?: "")
                setMsg("User Deleted.")
            } catch (e: Exception) {
                setMsg("Check your internet connection.")
            }
        }
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
            is UserEvent.SetFirstName -> {
                setFirstName(userEvent.fname)
            }
            is UserEvent.SetLastName -> {
                setLastName(userEvent.lname)
            }
            is UserEvent.SetEmail -> {
                setEmail(userEvent.email)
            }
            is UserEvent.UpdateUserCall -> {
                updateUserHandler()
            }
            is UserEvent.DeleteUserCall -> {
                userDeleteHandler()
            }
            is UserEvent.DismissMsg -> {
                dismissMsg()
            }
            is UserEvent.SetMSg -> {
                setMsg(userEvent.msg)
            }
        }
    }
}

val UserState by lazy { compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") } }
