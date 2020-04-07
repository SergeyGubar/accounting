package io.github.gubarsergey.accounting.redux.auth

import io.github.gubarsergey.accounting.redux.Reducer

data class AuthState(
    val token: String? = null,
    val needsRead: Boolean = true
)

object AuthReduce : Reducer<AuthState>({ authState, action ->
    when (action) {
        is ReadTokenAction -> {
            authState.copy(needsRead = true)
        }
        is TokenReadFinishedAction -> {
            authState.copy(token = action.token, needsRead = false)
        }
        else -> authState
    }
})