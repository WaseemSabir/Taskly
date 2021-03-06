package com.tajir.taskly.events

sealed class AuthenticationEvent {

    object ToggleAuthenticationMode: AuthenticationEvent()

    class EmailChanged(val emailAddress: String): AuthenticationEvent()

    class PasswordChanged(val password: String): AuthenticationEvent()

    class FirstNameChanged(val firstName: String): AuthenticationEvent()

    class LastNameChanged(val lastName: String): AuthenticationEvent()

    class SetError(val error: String): AuthenticationEvent()

    object Authenticate: AuthenticationEvent()

    object ErrorDismissed: AuthenticationEvent()

    object ClearAuthResponse: AuthenticationEvent()

    object Logout: AuthenticationEvent()
}
