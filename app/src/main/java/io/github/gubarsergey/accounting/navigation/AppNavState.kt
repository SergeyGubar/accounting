package io.github.gubarsergey.accounting.navigation

sealed class AppNavState {
    object Login: AppNavState()
    object Main: AppNavState()
}