package io.github.gubarsergey.accounting.ui.category.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.category.CategoryRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoryListInteractor(
    private val categoryRepository: CategoryRepository,
    private val _props: MutableLiveData<CategoryListFragment.Props>
) : ViewModel() {

    val props: LiveData<CategoryListFragment.Props> = _props

    init {
        viewModelScope.launch {
            categoryRepository.getMyCategories().fold({
                Timber.d("Load categories success: $it")
                _props.postValue(
                    CategoryListFragment.Props(
                        it.map { categoryDto ->
                            CategoryListFragment.Props.Category(categoryDto.id, categoryDto.title)
                        }
                    )
                )
            }, {
                Timber.e("$it")
            })
        }
    }
}