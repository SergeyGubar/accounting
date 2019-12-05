package io.github.gubarsergey.accounting

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props, State : Any, Action>(private var state: State) : ViewModel() {

    abstract fun map(state: State): Props

    protected fun dispatch(action: Action) {
        state = reduce(state, action)
        onNewProps(map(state))
    }

    abstract fun onNewProps(props: Props)
    abstract fun reduce(state: State, action: Action): State
}