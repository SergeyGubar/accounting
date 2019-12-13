package io.github.gubarsergey.accounting.ui.login

import io.github.gubarsergey.accounting.data.user.Credentials
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.redux.*
import io.github.gubarsergey.accounting.redux.login.LoginAction
import kotlinx.coroutines.*
import timber.log.Timber

class LoginConnector(
    private val userRepository: UserRepository
) : Connector<AppState, LoginFragment.Props> {

    override fun map(state: AppState, store: Store<AppState>): LoginFragment.Props {
        return if (state.loginState.isLoading) {
            LoginFragment.Props.Loading
        } else {
            LoginFragment.Props.Idle(
                state.loginState.email,
                state.loginState.password,
                state.loginState.emailError,
                state.loginState.passwordError,
                store.bindWith { email -> LoginAction.EmailUpdate(email) },
                store.bindWith { password -> LoginAction.PasswordUpdate(password) },
                Command {
                    launch {
                        withContext(Dispatchers.IO) {
                            userRepository.login(
                                Credentials(
                                    state.loginState.email,
                                    state.loginState.password
                                )
                            ).fold(
                                { token ->
                                    Timber.d("token $token")
                                },
                                { ex ->

                                }
                            )
                        }
                    }
                }
            )
        }
    }
}
