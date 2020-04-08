package io.github.gubarsergey.accounting.operator

import android.content.Context
import io.github.gubarsergey.accounting.redux.*
import io.github.gubarsergey.accounting.redux.auth.TokenReadFinishedAction
import io.github.gubarsergey.accounting.util.SharedPrefHelper
import timber.log.Timber

interface PropsConsumer<T> {
    fun consume(props: T)
}

val <T> PropsConsumer<T>.asConsumer
    get() = { item: T ->
        this.consume(item)
    }

class SharedPrefOperator(
    private val context: Context,
    private val prefHelper: SharedPrefHelper
) : PropsConsumer<SharedPrefOperator.Props> {

    private var lastToken: String? = null

    sealed class Props {
        object None : Props()
        data class ReadToken(val result: Command.With<String?>? = null) : Props()
        data class PutToken(val token: String, val result: Command? = null) : Props()
    }

    override fun consume(props: Props) {
        when (props) {
            is Props.ReadToken -> {
                if (lastToken != null) {
                    props.result?.invoke(lastToken)
                    return
                }
                val token = prefHelper.getToken(context)
                props.result?.invoke(token)
                this.lastToken = token
            }
            is Props.PutToken -> {
                Timber.d("Put token")
                if (props.token != lastToken) {
                    prefHelper.saveToken(context, props.token)
                    props.result?.invoke()
                    this.lastToken = props.token
                }
            }
        }
    }
}

class SharedPrefConnector : Connector<AppState, SharedPrefOperator.Props> {
    override fun map(state: AppState, store: Store<AppState>): SharedPrefOperator.Props {
        Timber.d("map ${state.authState.needsRead}")
        return when {
            state.authState.needsRead -> {
                SharedPrefOperator.Props.ReadToken(store.bindWith {
                    TokenReadFinishedAction(it)
                })
            }
            else -> {
                if (state.authState.token != null) {
                    return SharedPrefOperator.Props.PutToken(state.authState.token)
                }
                return SharedPrefOperator.Props.None
            }
        }
    }
}