package io.github.gubarsergey.accounting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.gubarsergey.accounting.redux.Mapper
import io.github.gubarsergey.accounting.redux.Reducer
import io.github.gubarsergey.accounting.redux.ReduxAction

abstract class BaseViewModel<Props, State : Any, Action : ReduxAction>(
    private var state: State,
    private val reducer: Reducer<State>,
    private val props: MutableLiveData<Props>
) : ViewModel() {

    protected abstract fun map(state: State): Props

    protected fun dispatch(action: Action) {
        state = reducer(state, action)
        props.postValue(map(state))
    }

}