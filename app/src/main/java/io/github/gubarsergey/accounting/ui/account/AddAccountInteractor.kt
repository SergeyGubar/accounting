package io.github.gubarsergey.accounting.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.data.account.CreateAccountDto
import io.github.gubarsergey.accounting.ui.Event
import io.github.gubarsergey.accounting.ui.transaction.list.BaseInteractor
import kotlinx.coroutines.launch
import timber.log.Timber

class AddAccountInteractor(
    private val accountsRepository: AccountsRepository,
    private val _events: MutableLiveData<Event<AddAccountEvent>>
) : BaseInteractor {

    sealed class AddAccountEvent {
        object AddAccountSuccess : AddAccountEvent()
        object AddAccountFailure : AddAccountEvent()
    }

    val events: LiveData<Event<AddAccountEvent>> = _events

    fun addAccount(title: String, currency: String, type: String, amount: Int) {
        launch {
            accountsRepository.addAccount(CreateAccountDto(title, currency, type, amount)).fold({
                Timber.e(it)
                _events.postValue(Event(AddAccountEvent.AddAccountFailure))
            }, {
                Timber.d("Success $it")
                _events.postValue(Event(AddAccountEvent.AddAccountSuccess))
            })

        }
    }
}