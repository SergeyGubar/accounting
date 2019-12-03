package io.github.gubarsergey.accounting

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props, State : Any, Action> : ViewModel() {
    abstract fun map(state: State): Props
    abstract val emptyState: State

    protected fun dispatch(action: Action) {
        state = reduce(state, action)
        onNewProps(map(state))
    }

    abstract fun onNewProps(props: Props)
    abstract fun reduce(state: State, action: Action): State
    protected lateinit var state: State
}