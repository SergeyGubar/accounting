package io.github.gubarsergey.accounting.redux

import android.os.Handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface Connector<State, Props>: CoroutineScope {
    fun map(state: State, store: Store<State>): Props
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main
}

typealias Consumer<Props> = (Props) -> Unit

inline fun <State, Props> Connector<State, Props>.connect(
    store: Store<State>,
    crossinline consumer: Consumer<Props>,
    handler: Handler = Handler()
) {
    store.addObserver(Observer(handler) { state ->
        consumer(map(state, store))
    })
}