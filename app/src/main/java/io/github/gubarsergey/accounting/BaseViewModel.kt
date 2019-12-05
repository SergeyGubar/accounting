package io.github.gubarsergey.accounting

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props, State : Any, Action>(private var state: State) : ViewModel() {

    protected abstract fun map(state: State): Props

    protected fun dispatch(action: Action) {
        state = reduce(state, action)
        onNewProps(map(state))
    }

    protected abstract fun onNewProps(props: Props)
    protected abstract fun reduce(state: State, action: Action): State
}