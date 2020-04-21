package io.github.gubarsergey.accounting.navigation

import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.auth.TokenReadFinishedAction
import io.github.gubarsergey.accounting.redux.login.LoginNetwork
import timber.log.Timber

object AppNavReduce : Reducer<AppNavState>({ state, action ->
    when (action) {
        is LoginNetwork.Finished -> AppNavState.Main
        is TokenReadFinishedAction -> {
            if (action.token != null) {
                AppNavState.Main
            } else {
                AppNavState.Login
            }
        }
        else -> AppNavState.Login
    }
})