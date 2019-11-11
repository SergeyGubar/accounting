package io.github.gubarsergey.accounting.ui.login

import io.github.gubarsergey.accounting.BaseViewModel

class LoginViewModel : BaseViewModel<LoginFragment.Props>() {

    init {
        props.value = LoginFragment.Props("", "", false)
    }
}
