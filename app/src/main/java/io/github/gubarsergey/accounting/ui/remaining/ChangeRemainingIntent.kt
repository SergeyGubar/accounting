package io.github.gubarsergey.accounting.ui.remaining

sealed class ChangeRemainingIntent {
    object ViewLoaded : ChangeRemainingIntent()
    data class ChangeRemaining(val accountPosition: Int, val remaining: String?): ChangeRemainingIntent()
}