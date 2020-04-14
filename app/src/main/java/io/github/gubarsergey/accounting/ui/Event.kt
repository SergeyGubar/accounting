package io.github.gubarsergey.accounting.ui

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun ifNotHandled(action: (T) -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            action(content)
        }
    }

    fun peekContent(): T = content
}