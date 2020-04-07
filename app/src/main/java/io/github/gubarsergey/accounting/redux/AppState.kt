package io.github.gubarsergey.accounting.redux

import io.github.gubarsergey.accounting.navigation.AppNavReduce
import io.github.gubarsergey.accounting.navigation.AppNavState
import io.github.gubarsergey.accounting.redux.auth.AuthReduce
import io.github.gubarsergey.accounting.redux.auth.AuthState
import io.github.gubarsergey.accounting.redux.login.LoginNetworkReduce
import io.github.gubarsergey.accounting.redux.login.LoginNetworkState
import io.github.gubarsergey.accounting.redux.login.LoginReduce
import io.github.gubarsergey.accounting.redux.login.LoginState

data class AppState(
    val navState: AppNavState = AppNavState.Login,
    val loginState: LoginState = LoginState(),
    val authState: AuthState = AuthState(),
    val loginNetworkState: LoginNetworkState = LoginNetworkState.None
)

object AppReducer : Reducer<AppState>({ state, action ->
    AppState(
        AppNavReduce(state.navState, action),
        LoginReduce(state.loginState, action),
        AuthReduce(state.authState, action),
        LoginNetworkReduce(state.loginNetworkState, action)
    )
})