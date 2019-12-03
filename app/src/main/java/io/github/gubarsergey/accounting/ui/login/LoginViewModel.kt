package io.github.gubarsergey.accounting.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.BaseViewModel
import io.github.gubarsergey.accounting.data.user.Credentials
import io.github.gubarsergey.accounting.data.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
    private val props: MutableLiveData<LoginFragment.Props>,
    private val userRepository: UserRepository
) : BaseViewModel<LoginFragment.Props, LoginViewModel.State, LoginViewModel.Action>() {

    data class State(
        val email: String,
        val password: String,
        val isLoading: Boolean,
        val emailError: String? = null,
        val passwordError: String? = null
    )

    sealed class Action {
        data class EmailUpdate(val email: String): Action()
        data class PasswordUpdate(val password: String): Action()
    }

    override val emptyState: State = State("", "", false)

    init {
        state = emptyState
    }

    fun emailUpdated(email: String) {
        dispatch(Action.EmailUpdate(email))
    }

    fun passwordUpdated(password: String) {
        dispatch(Action.PasswordUpdate(password))
    }

    fun login() {
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

    override fun map(state: State): LoginFragment.Props {
        return if (state.isLoading) {
            LoginFragment.Props.Loading
        } else {
            LoginFragment.Props.Idle(
                state.email,
                state.password,
                state.emailError,
                state.passwordError
            )
        }
    }

    override fun onNewProps(props: LoginFragment.Props) {
        this.props.postValue(props)
    }

    override fun reduce(state: State, action: Action): State {
        return when (action) {
            is Action.EmailUpdate -> state.copy(email = action.email, emailError = null)
            is Action.PasswordUpdate -> state.copy(password = action.password, passwordError = null)
        }
    }

}
