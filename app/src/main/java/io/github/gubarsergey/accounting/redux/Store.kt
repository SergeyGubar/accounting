package io.github.gubarsergey.accounting.redux

import android.os.Handler

class Observer<State>(
    private val handler: Handler = Handler(),
    private val observe: (State) -> Unit
) {

    operator fun invoke(state: State) {
        handler.post {
            observe(state)
        }
    }
}

class Store<State>(
    initialState: State,
    private val reducer: Reducer<State>,
    private val handler: Handler = Handler()
) {
    private val observers = mutableSetOf<Observer<State>>()
    private var state: State = initialState

    fun dispatch(action: ReduxAction) {
        handler.post {
            state = reducer.reduce(state, action)
            observers.forEach { fireState(state, it) }
        }
    }

    private fun fireState(state: State, observer: Observer<State>) {
        observer(state)
    }

    fun addObserver(observer: Observer<State>) {
        handler.post {
            require(observer !in observers) { "Observer $observer is already subscribed for updates of state" }

            observers += observer

            fireState(state, observer)
        }
    }
}

fun <State> Store<State>.bind(action: ReduxAction, also: () -> Unit) =
    Command { dispatch(action); also() }

fun <State> Store<State>.bind(action: ReduxAction) =
    Command { dispatch(action) }

inline fun <State> Store<State>.bind(actionCreator: () -> ReduxAction) = bind(actionCreator())

fun <State, T> Store<State>.bindWith(actionCreator: (T) -> ReduxAction) =
    Command.With<T> { dispatch(actionCreator(it)) }
