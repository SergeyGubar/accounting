package io.github.gubarsergey.accounting.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.transaction.TotalReport
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.ui.Event
import kotlinx.coroutines.launch


abstract class BaseInteractor<INTENT, EVENT, PROPS, STATE>(
    _state: STATE
) : ViewModel() {

    protected var state: STATE = _state
        set(value) {
            field = value
            val props = map(value)
            _props.postValue(props)
        }

    private val _props: MutableLiveData<PROPS> = MutableLiveData()
    private val _events: MutableLiveData<Event<EVENT>> = MutableLiveData()

    val props: LiveData<PROPS> = _props
    val events: LiveData<Event<EVENT>> = _events

    fun submit(intent: INTENT) {
        handle(intent)
    }

    protected fun submitEvent(event: EVENT) {
        _events.postValue(Event(event))
    }

    abstract fun handle(intent: INTENT)
    abstract fun map(state: STATE): PROPS

}

class AllTimeReportInteractor(
    private val transactionsRepository: TransactionsRepository
) : BaseInteractor<
        AllTimeReportInteractor.AllTimeReportIntent,
        AllTimeReportInteractor.AllTimeReportEvent,
        AllTimeReportFragment.Props,
        AllTimeReportInteractor.State
        >(
    State()
) {

    data class State(
        val reports: List<TotalReport?> = emptyList()
    )

    sealed class AllTimeReportIntent {
        object LoadReport : AllTimeReportIntent()
    }

    sealed class AllTimeReportEvent {
        data class LoadFailed(val reason: String) : AllTimeReportEvent()
    }

    override fun handle(intent: AllTimeReportIntent) {
        when (intent) {
            AllTimeReportIntent.LoadReport -> loadReport()
        }
    }

    private fun loadReport() {
        viewModelScope.launch {
            transactionsRepository.getTotalReport().fold({ err ->
                submitEvent(AllTimeReportEvent.LoadFailed(err.localizedMessage ?: "Unknown error"))
            }, { response ->
                state = state.copy(reports = response)
            })
        }
    }

    override fun map(state: State): AllTimeReportFragment.Props {
        return AllTimeReportFragment.Props(state.reports.mapNotNull { report ->
            report?.let {
                AllTimeReportFragment.Props.AccountReport(
                    it.id.id,
                    it.id.title,
                    it.totalEarned,
                    it.countEarned,
                    it.totalSpent,
                    it.countSpent
                )
            }
        })
    }
}