package io.github.gubarsergey.accounting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.navigation.ToolbarStatus
import io.github.gubarsergey.accounting.navigation.toToolbarStatus
import io.github.gubarsergey.accounting.util.makeInvisible
import io.github.gubarsergey.accounting.util.makeVisible
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            updateNavUi(destination)
        }
    }

    private fun updateNavUi(destination: NavDestination) {
        val toolbarStatus = destination.toToolbarStatus()
        when (toolbarStatus) {
            is ToolbarStatus.Invisible -> main_toolbar.makeInvisible()
            is ToolbarStatus.Visible -> with(main_toolbar) {
                makeVisible()
                setTitle(toolbarStatus.textRes)
            }
        }
    }
}
