package io.github.gubarsergey.accounting.ui.main

import androidx.lifecycle.ViewModel
import io.github.gubarsergey.accounting.BaseViewModel

class MainViewModel(state: State) :
    BaseViewModel<MainFragment.Props, MainViewModel.State, MainViewModel.Action>(state) {

    data class Action(val id: String)

    override fun map(state: State): MainFragment.Props {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    data class State(
        val record: List<String>
    )

    override fun onNewProps(props: MainFragment.Props) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reduce(state: State, action: Action): State {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
