package io.github.gubarsergey.accounting.redux

import io.github.gubarsergey.accounting.redux.login.LoginState
import io.github.gubarsergey.accounting.ui.login.LoginReduce

data class AppState(
    val loginState: LoginState = LoginState()
)

object AppReducer: Reducer<AppState>({ state, action ->
    AppState(
        LoginReduce(state.loginState, action)
    )
})