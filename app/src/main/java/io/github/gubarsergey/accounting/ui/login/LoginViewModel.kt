package io.github.gubarsergey.accounting.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.BaseViewModel
import io.github.gubarsergey.accounting.data.user.Credentials
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.redux.Command
import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.login.LoginAction
import io.github.gubarsergey.accounting.redux.login.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
    state: LoginState,
    reduce: Reducer<LoginState>,
    props: MutableLiveData<LoginFragment.Props>,
    private val userRepository: UserRepository
) : BaseViewModel<LoginFragment.Props, LoginState, LoginAction>(state, reduce, props) {

    override fun map(state: LoginState): LoginFragment.Props {
        return if (state.isLoading) {
            LoginFragment.Props.Loading
        } else {
            LoginFragment.Props.Idle(
                state.email,
                state.password,
                state.emailError,
                state.passwordError,
                Command.With { email ->
                    this.dispatch(LoginAction.EmailUpdate(email))
                },
                Command.With { password ->
                    this.dispatch(LoginAction.PasswordUpdate(password))
                },
                Command {
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            userRepository.login(Credentials(state.email, state.password))
                                .fold(
                                    { token ->
                                        Timber.d("token $token")
                                    },
                                    { ex ->
                                        Timber.e(ex)
                                    }
                                )
                        }
                    }
                }
            )
        }
    }
}
