package io.github.gubarsergey.accounting.redux.login

import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.redux.Reducer

sealed class LoginNetworkState {
    object None : LoginNetworkState()
    object Loading : LoginNetworkState()
    data class Finished(val token: String) : LoginNetworkState()
    data class FinishedWithError(val error: NetworkError) : LoginNetworkState()
}

object LoginNetworkReduce : Reducer<LoginNetworkState>({ state, action ->
    // TODO: Add LoginNetworkState.None
    when (action) {
        is LoginNetwork.Started -> LoginNetworkState.Loading
        is LoginNetwork.Finished -> LoginNetworkState.Finished(action.token)
        is LoginNetwork.FinishedWithError -> LoginNetworkState.FinishedWithError(action.error)
        else -> state
    }
})