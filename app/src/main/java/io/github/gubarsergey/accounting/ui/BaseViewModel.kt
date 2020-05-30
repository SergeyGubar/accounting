package io.github.gubarsergey.accounting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


abstract class BaseViewModel<INTENT, EVENT, PROPS, STATE>(
    _state: STATE
) : ViewModel() {

    protected var state: STATE = _state
        set(value) {
            field = value
            val props = map(value)
            _props.postValue(props)
        }

    private val _props: MutableLiveData<PROPS> = MutableLiveData()
    private val _events: MutableLiveData<Event<EVENT>> = MutableLiveData()

    val props: LiveData<PROPS> = _props
    val events: LiveData<Event<EVENT>> = _events

    fun submit(intent: INTENT) {
        handle(intent)
    }

    protected fun submitEvent(event: EVENT) {
        _events.postValue(Event(event))
    }

    abstract fun handle(intent: INTENT)
    abstract fun map(state: STATE): PROPS

}