package io.github.gubarsergey.accounting.redux

class Command(private val action: () -> Unit) {
    companion object {
        fun nop() = Command {}

        fun <T> nop() = With<T> {}
    }

    operator fun invoke() = action()

    class With<T>(private val action: (T) -> Unit) {
        operator fun invoke(value: T) = action(value)

        fun bind(value: T) = Command { action(value) }
    }
}
