package io.github.gubarsergey.accounting.redux.login

import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.redux.ReduxAction

sealed class LoginNetwork : ReduxAction {
    object Started : LoginNetwork()
    data class Finished(val token: String) : LoginNetwork()
    data class FinishedWithError(val error: NetworkError) : LoginNetwork()
}