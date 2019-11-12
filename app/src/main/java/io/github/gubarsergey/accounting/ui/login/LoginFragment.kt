package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment

import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.util.*
import kotlinx.android.synthetic.main.login_fragment.*
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

    override val layout: Int = R.layout.login_fragment
    override fun getProps() = viewModel.props

    // TODO: Inject viewmodel and get rid of getProps
    private val viewModel: LoginViewModel by viewModels()

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
            navigateToMainScreen()
        }
        login_email_edit_text.addSimpleTextChangeListener { email ->
            viewModel.emailUpdated(email)
        }
        login_password_edit_text.addSimpleTextChangeListener { password ->
            viewModel.passwordUpdated(password)
        }
    }

    private fun navigateToMainScreen() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
        findNavController().navigate(action)
    }
}
