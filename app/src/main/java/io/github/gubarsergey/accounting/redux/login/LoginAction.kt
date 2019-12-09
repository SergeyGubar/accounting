package io.github.gubarsergey.accounting.redux.login

import io.github.gubarsergey.accounting.redux.ReduxAction

sealed class LoginAction: ReduxAction {
    data class EmailUpdate(val email: String) : LoginAction()
    data class PasswordUpdate(val password: String) : LoginAction()
}