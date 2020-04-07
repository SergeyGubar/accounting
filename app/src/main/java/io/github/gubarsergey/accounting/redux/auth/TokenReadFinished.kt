package io.github.gubarsergey.accounting.redux.auth

import io.github.gubarsergey.accounting.redux.ReduxAction

data class TokenReadFinishedAction(val token: String?): ReduxAction