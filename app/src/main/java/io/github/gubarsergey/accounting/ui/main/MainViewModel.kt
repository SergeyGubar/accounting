package io.github.gubarsergey.accounting.ui.main

import androidx.lifecycle.ViewModel
import io.github.gubarsergey.accounting.BaseViewModel

class MainViewModel : BaseViewModel<MainFragment.Props, MainViewModel.State>() {
    override fun map(state: State): MainFragment.Props {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val emptyState: State
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    data class State(
        val record: List<String>
    )
}
