package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.redux.Command
import io.github.gubarsergey.accounting.util.addSimpleTextChangeListener
import io.github.gubarsergey.accounting.util.safelySetText
import io.github.gubarsergey.accounting.util.setViewDisabled
import io.github.gubarsergey.accounting.util.setViewEnabled
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class LoginFragment : BaseFragment<LoginFragment.Props>() {

    sealed class Props {
        data class Idle(
            val email: String,
            val password: String,
            val emailError: String?,
            val passwordError: String?,
            val emailUpdated: Command.With<String>,
            val passwordUpdated: Command.With<String>,
            val login: Command
        ) : Props()

        object Loading : Props()

        val idle: Idle? get() = this as? Idle
    }

    override val layout: Int = R.layout.fragment_login
    private val props: LiveData<Props> by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
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
            props.value?.idle?.login?.invoke()
        }
        login_email_edit_text.addSimpleTextChangeListener { email ->
            props.value?.idle?.emailUpdated?.invoke(email)
        }
        login_password_edit_text.addSimpleTextChangeListener { password ->
            props.value?.idle?.passwordUpdated?.invoke(password)
        }
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
    }
}
