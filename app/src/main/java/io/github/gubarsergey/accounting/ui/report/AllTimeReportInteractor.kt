package io.github.gubarsergey.accounting.ui.report

import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.transaction.TotalReport
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.ui.BaseViewModel
import kotlinx.coroutines.launch

class AllTimeReportInteractor(
    private val transactionsRepository: TransactionsRepository
) : BaseViewModel<
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