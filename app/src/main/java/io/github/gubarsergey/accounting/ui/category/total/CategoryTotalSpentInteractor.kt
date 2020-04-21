package io.github.gubarsergey.accounting.ui.category.total

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.ui.Event
import kotlinx.coroutines.launch

class CategoryTotalSpentInteractor(
    private val transactionsRepository: TransactionsRepository,
    private val _props: MutableLiveData<CategoryTotalSpentFragment.Props>,
    private val _events: MutableLiveData<Event<CategoryTotalSpentEvent>>
) : ViewModel() {

    val props: LiveData<CategoryTotalSpentFragment.Props> = _props
    val events: LiveData<Event<CategoryTotalSpentEvent>> = _events

    sealed class CategoryTotalSpentEvent {
        data class LoadFailed(val reason: String?) : CategoryTotalSpentEvent()
    }

    init {
        viewModelScope.launch {
            transactionsRepository.getCategoryTotalSpent()
                .fold({ err ->
                    _events.postValue(Event(CategoryTotalSpentEvent.LoadFailed(err.localizedMessage)))
                }, { result ->
                    _props.postValue(
                        CategoryTotalSpentFragment.Props(
                            result.map { dto ->
                                CategoryTotalSpentFragment.Props.TotalSpent(
                                    dto.id.categoryId,
                                    dto.id.name,
                                    dto.totalAmount,
                                    dto.count
                                )
                            }
                        )
                    )
                })
        }
    }
}