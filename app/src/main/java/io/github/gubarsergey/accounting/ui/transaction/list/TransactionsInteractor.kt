package io.github.gubarsergey.accounting.ui.transaction.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.transaction.TransactionDto
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

interface BaseInteractor : CoroutineScope {
    override val coroutineContext: CoroutineContext get() = Dispatchers.IO
}

class AccountsInteractor(
    private val accountsRepository: AccountsRepository,
    private val _props: MutableLiveData<TransactionsFragment.Props>
) : BaseInteractor {

    private var state = AccountsState()

    val props: LiveData<TransactionsFragment.Props> = _props

    data class AccountsState(
        val accountsInfo: Map<String, AccountInfo> = emptyMap()
    ) {
        data class AccountInfo(
            val title: String,
            val type: String,
            val ownerId: String,
            val currentAmount: Double,
            val transactions: List<TransactionDto> = emptyList()
        )
    }

    fun loadAccounts() {
        launch {
            accountsRepository.loadMyAccounts().fold({ accounts ->
                val state = if (accounts.isEmpty()) {
                    AccountsState()
                } else {
                    AccountsState(
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
                Timber.e("ERROR $it")
            })
        }
    }

    fun loadTransactions(accountId: String) {
        launch {
            accountsRepository.loadTransactions(accountId).fold({ result ->
                stateUpdated(
                    state.copy(accountsInfo = state.accountsInfo.map { kv ->
                        kv.key to if (kv.key == accountId) {
                            kv.value.copy(transactions = result)
                        } else {
                            kv.value
                        }
                    }.toMap())
                )
            }, {
                Timber.e("Error $it")
            })
        }
    }

    fun deleteTransaction(id: String) {
        launch {
            accountsRepository.deleteTransaction(id)
                .fold({ err ->
                    Timber.e(err)

                }, { dto ->
                    Timber.d("Deleted $dto")
                    // Most optimized state update EVER
                    stateUpdated(state.copy(accountsInfo = state.accountsInfo.map { kv ->
                        kv.key to kv.value.copy(
                            transactions = kv.value.transactions.filter { it.id != id },
                            currentAmount = if (kv.value.transactions.any { it.id == id }) kv.value.currentAmount - kv.value.transactions.first { it.id == id }.amount else kv.value.currentAmount
                        )
                    }.toMap()))
                })
        }
    }

    private fun stateUpdated(newState: AccountsState) {
        this.state = newState
        this._props.postValue(map(this.state))
    }

    private fun map(state: AccountsState): TransactionsFragment.Props {
        val format = SimpleDateFormat ("'Date:' yyyy-MM-dd")
        return if (state.accountsInfo.isEmpty()) {
            TransactionsFragment.Props()
        } else {
            TransactionsFragment.Props(
                state.accountsInfo.map { kv ->
                    TransactionsFragment.Props.AccountInfo(
                        kv.key,
                        kv.value.title,
                        kv.value.type,
                        kv.value.currentAmount,
                        kv.value.transactions.map {
                            TransactionsFragment.Props.Transaction(
                                it.id,
                                it.amount,
                                it.category.title,
                                it.message,
                                format.format(it.createdAt)
                            )
                        }
                    )
                }
            )
        }
    }
}