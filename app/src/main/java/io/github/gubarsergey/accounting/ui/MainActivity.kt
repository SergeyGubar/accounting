package io.github.gubarsergey.accounting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.databinding.ActivityMainBinding
import io.github.gubarsergey.accounting.navigation.*
import io.github.gubarsergey.accounting.ui.login.LoginFragmentDirections
import io.github.gubarsergey.accounting.util.inflater
import io.github.gubarsergey.accounting.util.makeGone
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

        // Temporary solution, let's say so
        navigationOperator
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.addTransactionFragment, R.id.addAccountFragment, R.id.addCategoryFragment, R.id.changeRemainingFragment -> {
                    binding.bottomNavigation.makeGone()
                }
                else -> {
                    binding.bottomNavigation.makeVisible()
                }
            }

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

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            findNavController(R.id.nav_host_fragment)
        )
    }

    override fun navigate(navState: NavProps) {
        val navController = findNavController(R.id.nav_host_fragment)
        when (navState) {
            NavProps.LOGIN -> navController.navigate(R.id.loginFragment)
            NavProps.MAIN_FRAGMENT -> navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
    }
}
