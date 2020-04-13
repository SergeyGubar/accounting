package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import arrow.optics.optics
import io.github.gubarsergey.accounting.BaseFragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.FragmentLoginBinding
import io.github.gubarsergey.accounting.operator.SharedPrefOperator
import io.github.gubarsergey.accounting.redux.Command
import io.github.gubarsergey.accounting.redux.nop
import io.github.gubarsergey.accounting.util.addSimpleTextChangeListener
import io.github.gubarsergey.accounting.util.safelySetText
import io.github.gubarsergey.accounting.util.setViewDisabled
import io.github.gubarsergey.accounting.util.setViewEnabled
import org.koin.android.ext.android.inject
import timber.log.Timber


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    sealed class Props {
        data class Idle(
            val email: String,
            val password: String,
            val emailError: String?,
            val passwordError: String?,
            val emailUpdated: Command.With<String>,
            val passwordUpdated: Command.With<String>,
            val login: Command,
            val didLoad: Command
        ) : Props()

        object Loading : Props()
    }

    // Functional optics for poor people (very poor)
    private val Props?.idleUnsafe get() = this as Props.Idle
    private val Props?.idleSafe get() = this as? Props.Idle

    private val props: LiveData<Props> by inject()
    private val sharedPrefOperator: SharedPrefOperator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        props.observe(viewLifecycleOwner, Observer {
            render(it)
        })
        sharedPrefOperator
        props.value.idleSafe?.didLoad?.invoke()
    }

    private fun render(props: Props) {
        Timber.d("render props $props")
        when (props) {
            is Props.Idle -> {
                binding.loginEmailEditText.setViewEnabled()
                binding.loginPasswordEditText.setViewEnabled()
                binding.loginButton.setViewEnabled()
                binding.loginEmailEditText.safelySetText(props.email)
                binding.loginPasswordEditText.safelySetText(props.password)
            }
            is Props.Loading -> {
                binding.loginEmailEditText.setViewDisabled()
                binding.loginPasswordEditText.setViewDisabled()
                binding.loginButton.setViewDisabled()
                // TODO: Show loading
            }
        }

    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            props.value.idleUnsafe.login.invoke()
        }
        binding.loginEmailEditText.addSimpleTextChangeListener { email ->
            props.value.idleUnsafe.emailUpdated.invoke(email)
        }
        binding.loginPasswordEditText.addSimpleTextChangeListener { password ->
            props.value.idleUnsafe.passwordUpdated.invoke(password)
        }
    }
}
