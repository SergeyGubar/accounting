package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment

import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.navigation.NavigationState
import io.github.gubarsergey.accounting.navigation.Router
import io.github.gubarsergey.accounting.util.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFragment : BaseFragment<LoginFragment.Props>() {

    sealed class Props {
        data class Idle(
            val email: String,
            val password: String,
            val emailError: String? = null,
            val passwordError: String? = null
        ) : Props()

        object Loading : Props()
    }

    override val layout: Int = R.layout.fragment_login
    override fun getProps() = vm.props
    private val vm: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun render(props: Props) {
        Timber.d("render props $props")
        when (props) {
            is Props.Idle -> {
                login_email_edit_text.setViewEnabled()
                login_password_edit_text.setViewEnabled()
                login_button.setViewEnabled()
                login_email_edit_text.safelySetText(props.email)
                login_password_edit_text.safelySetText(props.password)
            }
            is Props.Loading -> {
                login_email_edit_text.setViewDisabled()
                login_password_edit_text.setViewDisabled()
                login_button.setViewDisabled()
                // TODO: Show loading
            }
        }

    }

    private fun setupListeners() {
        login_button.setOnClickListener {
            vm.login()
        }
        login_email_edit_text.addSimpleTextChangeListener { email ->
            vm.emailUpdated(email)
        }
        login_password_edit_text.addSimpleTextChangeListener { password ->
            vm.passwordUpdated(password)
        }
    }

    private fun navigateToMainScreen() {
        val action = Router.getNavigateAction(NavigationState.LOGIN, NavigationState.MAIN_FRAGMENT)
        findNavController().navigate(action)
    }
}
