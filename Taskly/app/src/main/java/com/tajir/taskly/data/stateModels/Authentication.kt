package com.tajir.taskly.data.stateModels

import androidx.annotation.StringRes
import com.tajir.taskly.R
import com.tajir.taskly.data.api.models.GeneralResponseAuth
import retrofit2.Response
import kotlin.collections.List


enum class AuthenticationMode {
    SIGN_UP, SIGN_IN
}

enum class PasswordRequirement(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.password_requirement_capital),
    NUMBER(R.string.password_requirement_digit),
    EIGHT_CHARACTERS(R.string.password_requirement_characters)
}

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val passwordRequirements: List<PasswordRequirement> = emptyList(),
    val isLoading: Boolean = false,
    val authResponse: Response<GeneralResponseAuth>? = null,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return password?.isNotEmpty() == true &&
                email?.isNotEmpty() == true &&
                (authenticationMode == AuthenticationMode.SIGN_IN
                        || passwordRequirements.containsAll(
                    PasswordRequirement.values().toList())) &&
                (authenticationMode == AuthenticationMode.SIGN_IN
                        || first_name?.isNotEmpty() == true) &&
                (authenticationMode == AuthenticationMode.SIGN_IN
                        || last_name?.isNotEmpty() == true)
    }
}
