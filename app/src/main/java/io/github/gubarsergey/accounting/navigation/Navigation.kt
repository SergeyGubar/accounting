package io.github.gubarsergey.accounting.navigation

import androidx.navigation.NavDestination
import io.github.gubarsergey.accounting.R



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