package io.github.gubarsergey.accounting.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import io.github.gubarsergey.accounting.R
import io.github.gubarsergey.accounting.ui.login.LoginFragmentDirections
import java.lang.IllegalStateException


object Router {
    fun getNavigateAction(currentState: NavigationState, desiredState: NavigationState): NavDirections {
        return when(currentState to desiredState) {
            NavigationState.LOGIN to NavigationState.MAIN_FRAGMENT-> LoginFragmentDirections.actionLoginFragmentToMainFragment()
            else -> throw IllegalStateException("Navigating from $currentState to $desiredState is impossible")
        }
    }
}


enum class NavigationState(
    val destinationId: Int,
    val toolbarStatus: ToolbarStatus
) {
    LOGIN(R.id.loginFragment, ToolbarStatus.Invisible),
    MAIN_FRAGMENT(R.id.mainFragment, ToolbarStatus.Visible(R.string.app_name))
}

sealed class ToolbarStatus {
    object Invisible : ToolbarStatus()
    data class Visible(val textRes: Int) : ToolbarStatus()
}

fun NavDestination.toToolbarStatus() =
    NavigationState.values().first { it.destinationId == this.id }.toolbarStatus