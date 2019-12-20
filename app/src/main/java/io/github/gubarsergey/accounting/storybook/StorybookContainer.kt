package io.github.gubarsergey.accounting.storybook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.redux.nop
import io.github.gubarsergey.accounting.redux.nope
import io.github.gubarsergey.accounting.ui.login.LoginFragment

interface StorybookActionListener {
    fun showLogin(props: LoginFragment.Props)
}

class StorybookContainer : Fragment() {

    private lateinit var storybook: StorybookActionListener

    private val cases = hashMapOf(
        "Login empty" to {
            storybook.showLogin(
                LoginFragment.Props.Idle(
                    "",
                    "",
                    null,
                    null,
                    nope(),
                    nope(),
                    nop()
                )
            )
        },
        "Login loading" to {
            LoginFragment.Props.Loading
        },
        "Login error" to { },
        "Login correct data" to { }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is StorybookActionListener) { "Hosting activity must listen for storybook actions" }
        storybook = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_storybook, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cases.forEach { case ->
            val button = Button(requireContext())
            button.text = case.key
            button.setOnClickListener {
                case.value()
            }
        }
    }
}