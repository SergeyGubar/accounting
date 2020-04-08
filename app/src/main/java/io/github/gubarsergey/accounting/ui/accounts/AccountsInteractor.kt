package io.github.gubarsergey.accounting.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

interface BaseInteractor : CoroutineScope {
    override val coroutineContext: CoroutineContext get() = Dispatchers.IO
}

class AccountsInteractor(
    private val accountsRepository: AccountsRepository,
    private val _props: MutableLiveData<AccountsFragment.Props>
) : BaseInteractor {

    private var state = AccountsState()

    val props: LiveData<AccountsFragment.Props> = _props

    data class AccountsState(
        val selectedAccountId: String? = null,
        val accountsInfo: Map<String, AccountInfo> = emptyMap()
    ) {
        data class AccountInfo(
            val title: String,
            val type: String,
            val ownerId: String,
            val currentAmount: Int
        )
    }

    fun loadAccounts() {
        launch {
            accountsRepository.loadMyAccounts().fold({ accounts ->
                val state = if (accounts.isEmpty()) {
                    AccountsState()
                } else {
                    AccountsState(
                        accounts.first().id,
                        accounts.associateBy({ it.id },
                            {
                                AccountsState.AccountInfo(
                                    it.title,
                                    it.type,
                                    it.ownerId,
                                    it.currentAmount
                                )
                            })
                    )
                }
                stateUpdated(state)
            }, {
                Timber.e("ERROR")
            })
        }
    }

    private fun stateUpdated(newState: AccountsState) {
        this.state = newState
        this._props.postValue(map(this.state))
    }

    private fun map(state: AccountsState): AccountsFragment.Props {
        val accountInfo = state.accountsInfo[state.selectedAccountId]

        return if (accountInfo == null) {
            AccountsFragment.Props()
        } else {
            AccountsFragment.Props(accountInfo.title, accountInfo.type, emptyList())
        }
    }
}