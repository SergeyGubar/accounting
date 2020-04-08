package io.github.gubarsergey.accounting.redux.login

import io.github.gubarsergey.accounting.redux.Reducer

data class LoginState(
    val email: String = "sergey.gubar187@gmail.com",
    val password: String = "megubar123",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

object LoginReduce : Reducer<LoginState>({ state, action ->
    when (action) {
        is LoginAction.EmailUpdate -> state.copy(email = action.email, emailError = null)
        is LoginAction.PasswordUpdate -> state.copy(
            password = action.password,
            passwordError = null
        )
        else -> state
    }
})