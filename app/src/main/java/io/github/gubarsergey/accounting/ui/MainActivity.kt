package io.github.gubarsergey.accounting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.ActivityMainBinding
import io.github.gubarsergey.accounting.navigation.*
import io.github.gubarsergey.accounting.ui.login.LoginFragmentDirections
import io.github.gubarsergey.accounting.util.inflater
import io.github.gubarsergey.accounting.util.makeInvisible
import io.github.gubarsergey.accounting.util.makeVisible
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity(), Router {

    private val navigationOperator: NavigationOperator by inject {
        parametersOf(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(inflater)
        setSupportActionBar(binding.mainToolbar)
        // Temporary solution, let's say so
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (val status = destination.toToolbarStatus()) {
                is ToolbarStatus.Visible -> {
                    binding.mainToolbar.makeVisible()
                    binding.mainToolbar.setTitle(status.textRes)
                }
                is ToolbarStatus.Invisible -> {
                    binding.mainToolbar.makeInvisible()
                }
            }
        }
        navigationOperator
    }

    override fun navigate(navState: NavProps) {
        val navController = findNavController(R.id.nav_host_fragment)
        when (navState) {
            NavProps.LOGIN -> navController.navigate(R.id.loginFragment)
            NavProps.MAIN_FRAGMENT -> navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
    }
}
