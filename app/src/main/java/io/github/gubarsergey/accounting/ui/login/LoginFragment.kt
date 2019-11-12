package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.gubarsergey.accounting.BaseFragment

import io.github.gubarsergey.accounting.R
import kotlinx.android.synthetic.main.login_fragment.*
import timber.log.Timber


class LoginFragment : BaseFragment<LoginFragment.Props>() {

    data class Props(
        val email: String,
        val password: String,
        val isLoadingShown: Boolean
    )

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        viewModel.props.observe(viewLifecycleOwner, Observer { props ->
            render(props)
        })
    }

    override fun render(props: Props) {
        Timber.d("render props $props")
        login_email_edit_text.setText(props.email)
        login_password_edit_text.setText(props.password)
    }

    private fun setupListeners() {
        // TODO: Interact with viewmodel
        Timber.d("setupListeners")
        login_button.setOnClickListener {
            Timber.d("navigate to login")
            val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

}
