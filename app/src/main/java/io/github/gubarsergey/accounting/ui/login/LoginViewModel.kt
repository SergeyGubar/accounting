package io.github.gubarsergey.accounting.ui.login

import io.github.gubarsergey.accounting.BaseViewModel

class LoginViewModel : BaseViewModel<LoginFragment.Props, LoginViewModel.State>() {

    data class State(
        val email: String,
        val password: String,
        val isLoading: Boolean,
        val emailError: String? = null,
        val passwordError: String? = null
    )

    override val emptyState: State = State("", "", false)

    fun emailUpdated(email: String) {
        state = state.copy(email = email)
    }

    fun passwordUpdated(password: String) {
        state = state.copy(password = password)
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
