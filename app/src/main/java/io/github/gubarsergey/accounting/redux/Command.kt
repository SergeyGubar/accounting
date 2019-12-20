package io.github.gubarsergey.accounting.redux

class Command(private val action: () -> Unit) {
    companion object {
        fun nop() = Command {}

    }

    operator fun invoke() = action()

    class With<T>(private val action: (T) -> Unit) {
        companion object {
            fun <T> nop() = With<T> {}
        }

        operator fun invoke(value: T) = action(value)

        fun bind(value: T) = Command { action(value) }
    }
}


fun <T> nope() = Command.With.nop<T>()
fun nop() = Command.nop()