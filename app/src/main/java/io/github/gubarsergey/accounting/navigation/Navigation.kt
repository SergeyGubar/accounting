package io.github.gubarsergey.accounting.navigation

import io.github.gubarsergey.accounting.R

enum class NavProps(
    val toolbarStatus: ToolbarStatus
) {
    LOGIN(ToolbarStatus.Invisible),
    MAIN_FRAGMENT(ToolbarStatus.Visible(R.string.app_name))
}

sealed class ToolbarStatus {
    object Invisible : ToolbarStatus()
    data class Visible(val textRes: Int) : ToolbarStatus()
}