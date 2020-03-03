package io.github.gubarsergey.accounting.operator

import android.content.Context
import io.github.gubarsergey.accounting.redux.Command

interface BaseOperator<Props> {
    val props: Props
}

val <Props> BaseOperator<Props>.asConsumer
    get() = { newProps: Props ->
        if (this.props != newProps) {

        }
    }

class SharedPrefOperator(
    private val context: Context
) {
    sealed class Props {
        object None
        data class ReadToken(val result: Command.With<String>)
        data class PutToken(val token: String)
    }

}