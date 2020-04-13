package io.github.gubarsergey.accounting.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.account.Account
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.data.category.CategoryDto
import io.github.gubarsergey.accounting.data.category.CategoryRepository
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.ui.accounts.BaseInteractor
import kotlinx.coroutines.launch
import timber.log.Timber

class AddTransactionsInteractor(
    private val categoryRepository: CategoryRepository,
    private val accountsRepository: AccountsRepository,
    private val transactionsRepository: TransactionsRepository,
    private val _props: MutableLiveData<AddTransactionFragment.Props>,
    private val _events: MutableLiveData<AddTransactionFragment.Events>
) : BaseInteractor {

    val props: LiveData<AddTransactionFragment.Props> = _props
    val events: LiveData<AddTransactionFragment.Events> = _events

    data class State(
        val categories: List<CategoryDto> = listOf(),
        val accounts: List<Account> = listOf()
    )

    private var state = State()


    fun loadCategories() {
        launch {
            categoryRepository.getMyCategories().fold({
                stateUpdated(state.copy(categories = it))
            }, {
                Timber.e("Error $it")
            })
        }
    }

    fun loadAccounts() {
        launch {
            accountsRepository.loadMyAccounts().fold({
                stateUpdated(state.copy(accounts = it))
            }, {
            })
        }
    }

    fun addTransaction(accountIndex: Int, amount: Int, categoryIndex: Int) {
        launch {
            transactionsRepository.addTransaction(
                state.accounts[accountIndex].id,
                amount,
                state.categories[categoryIndex].id
            ).fold({
                _events.postValue(AddTransactionFragment.Events.Success)
            }, {
                _events.postValue(AddTransactionFragment.Events.Error)
            })
        }
    }

    private fun stateUpdated(state: State) {
        this.state = state
        _props.postValue(AddTransactionFragment.Props(state.categories, state.accounts))
    }
}