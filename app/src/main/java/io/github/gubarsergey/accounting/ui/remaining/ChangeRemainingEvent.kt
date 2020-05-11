package io.github.gubarsergey.accounting.ui.remaining

sealed class ChangeRemainingEvent {
    object LoadFailed : ChangeRemainingEvent()
    object ValidationFailed : ChangeRemainingEvent()
    object SaveFailed : ChangeRemainingEvent()
    object SaveSuccess : ChangeRemainingEvent()
}