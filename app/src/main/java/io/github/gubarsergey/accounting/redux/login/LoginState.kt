package io.github.gubarsergey.accounting.redux.login

data class LoginState(
    val email: String,
    val password: String,
    val isLoading: Boolean,
    val emailError: String? = null,
    val passwordError: String? = null
)