package com.tajir.taskly.events

sealed class UserEvent{

    class TokenChanged(val token: String): UserEvent()

    class SetFirstName(val fname: String): UserEvent()

    class SetLastName(val lname: String): UserEvent()

    class SetEmail(val email: String): UserEvent()

    object UpdateUserCall : UserEvent()

    object DeleteUserCall : UserEvent()

    object Logout : UserEvent()

    object DismissMsg : UserEvent()

    class SetMSg(val msg : String) : UserEvent()
}
