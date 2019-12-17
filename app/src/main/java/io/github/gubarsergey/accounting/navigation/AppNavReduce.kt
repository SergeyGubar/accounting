package io.github.gubarsergey.accounting.navigation

import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.login.LoginNetwork

object AppNavReduce : Reducer<AppNavState>({ state, action ->
    when (action) {
        is LoginNetwork.Finished -> AppNavState.Main
        else -> AppNavState.Login
    }
})