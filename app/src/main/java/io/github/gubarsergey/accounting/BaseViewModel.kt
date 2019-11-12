package io.github.gubarsergey.accounting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props, State : Any> : ViewModel() {
    val props = MutableLiveData<Props>()
    abstract fun map(state: State): Props
    abstract val emptyState: State
    protected var state: State = emptyState
        set(value) {
            props.value = map(value)
            field = value
        }
}