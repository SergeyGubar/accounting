package io.github.gubarsergey.accounting.navigation

import io.github.gubarsergey.accounting.redux.AppState
import io.github.gubarsergey.accounting.redux.Connector
import io.github.gubarsergey.accounting.redux.Store

interface Router {
    fun navigate(navState: NavProps)
}

class NavigationOperator(
    private var navProps: NavProps,
    private val router: Router
) {
    fun asConsumer() = { props: NavProps ->
        if (this.navProps != props) {
            this.navProps = props
            router.navigate(navProps)
        }
    }
}

class NavigationConnector : Connector<AppState, NavProps> {
    override fun map(state: AppState, store: Store<AppState>): NavProps {
        return when (state.navState) {
            is AppNavState.Login -> NavProps.LOGIN
            is AppNavState.Main -> NavProps.MAIN_FRAGMENT
        }
    }
}