package io.github.gubarsergey.accounting.ui.login

import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.login.LoginAction
import io.github.gubarsergey.accounting.redux.login.LoginState

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