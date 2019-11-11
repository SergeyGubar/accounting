package io.github.gubarsergey.accounting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Props>: ViewModel() {
    val props = MutableLiveData<Props>()
}