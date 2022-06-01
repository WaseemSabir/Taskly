package com.tajir.taskly.events

sealed class UserEvent{

    class TokenChanged(val token: String): UserEvent()

    object Logout : UserEvent()
}
