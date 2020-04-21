package io.github.gubarsergey.accounting.ui.category.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.category.CategoryRepository
import io.github.gubarsergey.accounting.data.category.CreateCategoryDto
import io.github.gubarsergey.accounting.ui.Event
import io.github.gubarsergey.accounting.ui.transaction.list.BaseInteractor
import kotlinx.coroutines.launch
import timber.log.Timber

class AddCategoryInteractor(
    private val categoryRepository: CategoryRepository,
    private val _events: MutableLiveData<Event<AddCategoryEvent>>
) : BaseInteractor {

    val events: LiveData<Event<AddCategoryEvent>> = _events

    sealed class AddCategoryEvent {
        object Success : AddCategoryEvent()
        object Failure : AddCategoryEvent()
    }

    fun addCategory(title: String) {
        launch {
            categoryRepository.addCategory(CreateCategoryDto(title)).fold({
                Timber.e(it)
                _events.postValue(Event(AddCategoryEvent.Failure))
            }, {
                _events.postValue(Event(AddCategoryEvent.Success))
            })
        }
    }
}