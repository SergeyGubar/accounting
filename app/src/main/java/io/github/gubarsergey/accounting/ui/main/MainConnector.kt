package io.github.gubarsergey.accounting.ui.main

import io.github.gubarsergey.accounting.redux.Connector
import io.github.gubarsergey.accounting.redux.Store

class MainConnector : Connector<MainConnector.State, MainFragment.Props> {

    data class State(
        val record: List<String>
    )

    override fun map(state: State, store: Store<State>): MainFragment.Props {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
