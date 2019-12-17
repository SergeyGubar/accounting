package io.github.gubarsergey.accounting.navigation

import androidx.navigation.NavDestination
import io.github.gubarsergey.accounting.R
import java.lang.IllegalArgumentException

enum class NavProps {
    LOGIN,
    MAIN_FRAGMENT
}

sealed class ToolbarStatus {
    object Invisible : ToolbarStatus()
    data class Visible(val textRes: Int) : ToolbarStatus()
}

fun NavDestination.toToolbarStatus(): ToolbarStatus {
    return when (this.id) {
        R.id.loginFragment -> ToolbarStatus.Invisible
        R.id.mainFragment -> ToolbarStatus.Visible(R.string.app_name)
        else -> throw IllegalArgumentException("Destination $this has no toolbar status!")
    }
}