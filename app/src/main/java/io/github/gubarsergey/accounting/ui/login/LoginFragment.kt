package io.github.gubarsergey.accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import io.github.gubarsergey.accounting.BaseFragment

import io.github.gubarsergey.accounting.R
import timber.log.Timber


class LoginFragment : BaseFragment<LoginFragment.Props>() {

    data class Props(
        val email: String,
        val password: String,
        val isLoadingShown: Boolean
    )

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.props.observe(viewLifecycleOwner, Observer { props ->
            render(props)
        })
        setupListeners()
    }

    override fun render(props: Props) {
        Timber.d("render props $props")
    }

    private fun setupListeners() {

    }

}
