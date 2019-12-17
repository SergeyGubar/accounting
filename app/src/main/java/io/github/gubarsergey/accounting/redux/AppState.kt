package io.github.gubarsergey.accounting.redux

import io.github.gubarsergey.accounting.navigation.AppNavReduce
import io.github.gubarsergey.accounting.navigation.AppNavState
import io.github.gubarsergey.accounting.redux.login.LoginNetworkReduce
import io.github.gubarsergey.accounting.redux.login.LoginNetworkState
import io.github.gubarsergey.accounting.redux.login.LoginReduce
import io.github.gubarsergey.accounting.redux.login.LoginState

data class AppState(
    val navState: AppNavState = AppNavState.Login,
    val loginState: LoginState = LoginState(),
    val loginNetworkState: LoginNetworkState = LoginNetworkState.None
)

object AppReducer : Reducer<AppState>({ state, action ->
    AppState(
        AppNavReduce(state.navState, action),
        LoginReduce(state.loginState, action),
        LoginNetworkReduce(state.loginNetworkState, action)
    )
})