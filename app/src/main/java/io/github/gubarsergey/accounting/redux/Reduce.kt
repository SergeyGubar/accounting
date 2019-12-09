package io.github.gubarsergey.accounting.redux

abstract class Reducer<State>(val reduce: (State, ReduxAction) -> State) {
    operator fun invoke(state: State, action: ReduxAction) = reduce(state, action)
}