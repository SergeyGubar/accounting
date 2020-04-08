package io.github.gubarsergey.accounting.redux.auth

import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.login.LoginNetwork

data class AuthState(
    val token: String? = null,
    val needsRead: Boolean = true
)

object AuthReduce : Reducer<AuthState>({ authState, action ->
    when (action) {
        is ReadTokenAction -> {
            authState.copy(needsRead = true)
        }
        is LoginNetwork.Finished -> {
            authState.copy(needsRead = false, token = action.token)
        }
        is TokenReadFinishedAction -> {
            authState.copy(token = action.token, needsRead = false)
        }
        else -> authState
    }
})