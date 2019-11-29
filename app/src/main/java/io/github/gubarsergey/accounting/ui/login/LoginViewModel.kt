package io.github.gubarsergey.accounting.ui.login

import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.BaseViewModel
import io.github.gubarsergey.accounting.data.ApiFactory
import io.github.gubarsergey.accounting.data.user.Credentials
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.data.user.UserRemoteDataSource
import io.github.gubarsergey.accounting.data.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel : BaseViewModel<LoginFragment.Props, LoginViewModel.State>() {

    data class State(
        val email: String,
        val password: String,
        val isLoading: Boolean,
        val emailError: String? = null,
        val passwordError: String? = null
    )

    // DI Users cry here
    private val userRepository =
        UserRepository(UserRemoteDataSource(ApiFactory.retrofit().create(UserApi::class.java)))

    override val emptyState: State = State("", "", false)

    init {
        state = emptyState
    }

    fun emailUpdated(email: String) {
        state = state.copy(email = email)
        stateChanged()
    }

    fun passwordUpdated(password: String) {
        state = state.copy(password = password)
        stateChanged()
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

}
