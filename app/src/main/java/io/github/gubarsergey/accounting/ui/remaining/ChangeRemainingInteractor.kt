package io.github.gubarsergey.accounting.ui.remaining

import androidx.lifecycle.viewModelScope
import arrow.core.extensions.either.foldable.fold
import io.github.gubarsergey.accounting.data.account.Account
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.ui.report.BaseInteractor
import kotlinx.coroutines.launch

class ChangeRemainingInteractor(
    private val accountsRepository: AccountsRepository
) : BaseInteractor<ChangeRemainingIntent, ChangeRemainingEvent, ChangeRemainingFragment.Props, ChangeRemainingInteractor.State>(
    State()
) {

    data class State(
        val selectedAccount: String? = null,
        val amount: Int? = null,
        val accounts: List<Account>? = null
    )

    override fun handle(intent: ChangeRemainingIntent) {
        when (intent) {
            ChangeRemainingIntent.ViewLoaded -> loadAccounts()
            is ChangeRemainingIntent.ChangeRemaining -> changeRemaining(
                intent.accountPosition,
                intent.remaining
            )
        }
    }

    private fun changeRemaining(accountPosition: Int, remaining: String?) {
        if (accountPosition == -1 || remaining.isNullOrBlank()) {
            submitEvent(ChangeRemainingEvent.ValidationFailed)
            return
        }

        viewModelScope.launch {
            accountsRepository.changeRemaining(
                state.accounts!![accountPosition].id,
                remaining.toInt()
            ).fold({
                submitEvent(ChangeRemainingEvent.SaveFailed)
            }, {
                submitEvent(ChangeRemainingEvent.SaveSuccess)
            })
        }
    }

    override fun map(state: State): ChangeRemainingFragment.Props {
        return ChangeRemainingFragment.Props(
            state.selectedAccount,
            state.accounts?.map { it.id to it.title } ?: listOf()
        )
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            accountsRepository.loadMyAccounts().fold(
                { response ->
                    if (response.isNotEmpty()) {
                        state = state.copy(
                            accounts = response,
                            selectedAccount = response.first().id
                        )
                    }
                },
                {
                    submitEvent(ChangeRemainingEvent.LoadFailed)
                }
            )
        }
    }
}