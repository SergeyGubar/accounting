package io.github.gubarsergey.accounting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.navigation.NavigationOperator
import io.github.gubarsergey.accounting.navigation.NavProps
import io.github.gubarsergey.accounting.navigation.Router
import io.github.gubarsergey.accounting.navigation.ToolbarStatus
import io.github.gubarsergey.accounting.ui.login.LoginFragmentDirections
import io.github.gubarsergey.accounting.util.makeInvisible
import io.github.gubarsergey.accounting.util.makeVisible
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity(), Router {

    private val navigationOperator: NavigationOperator by inject {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        // Temporary solution, let's say so
        navigationOperator
    }

    override fun navigate(navState: NavProps) {
        val navController = findNavController(R.id.nav_host_fragment)
        when (navState) {
            NavProps.LOGIN -> {
                navController.navigate(R.id.loginFragment)
            }
            NavProps.MAIN_FRAGMENT -> {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
            }
        }
        when (navState.toolbarStatus) {
            is ToolbarStatus.Invisible -> main_toolbar.makeInvisible()
            is ToolbarStatus.Visible -> with(main_toolbar) {
                makeVisible()
                setTitle(navState.toolbarStatus.textRes)
            }
        }
    }
}
