package io.github.gubarsergey.accounting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props, State : Any> : ViewModel() {
    val props = MutableLiveData<Props>()
    abstract fun map(state: State): Props
    abstract val emptyState: State
    protected lateinit var state: State

    fun stateChanged() {
        // TODO: Get rid of it
        props.value = map(state)
    }
}