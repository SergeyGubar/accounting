package io.github.gubarsergey.accounting.ui.transaction.report

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.account.Account
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.data.transaction.DailySpentReport
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.ui.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


private data class State(
    val accounts: List<Account> = emptyList(),
    val startTime: Date? = null,
    val endTime: Date? = null,
    val results: List<DailySpentReport> = emptyList(),
    val selectedId: String? = null
)

class TimeRangeReportInteractor(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository,
    private val _props: MutableLiveData<TimeRangeReportFragment.Props>,
    private val _events: MutableLiveData<Event<TimeRangeReportEvent>>
) : ViewModel() {

    private var state: State = State()
        set(value) {
            field = value
            map()
        }

    val props: LiveData<TimeRangeReportFragment.Props> = _props
    val events: LiveData<Event<TimeRangeReportEvent>> = _events

    sealed class TimeRangeReportEvent {
        object InputNotValid : TimeRangeReportEvent()
        object NetworkError : TimeRangeReportEvent()
    }

    init {
        viewModelScope.launch {
            accountsRepository.loadMyAccounts().fold(
                { result ->
                    val selectedId = if (result.isNotEmpty()) result.first().id else null
                    state = state.copy(accounts = result, selectedId = selectedId)
                },
                { err ->
                    _events.postValue(Event(TimeRangeReportEvent.NetworkError))
                }
            )
        }

    }

    fun startTimeSelected(time: Date) {
        state = state.copy(startTime = time)
    }

    fun endTimeSelected(time: Date) {
        state = state.copy(endTime = time)
    }

    fun selectAccount(position: Int) {
        state = state.copy(selectedId = state.accounts[position].id)
    }

    @SuppressLint("SimpleDateFormat")
    fun generateReport() {
        if (state.startTime == null || state.endTime == null || state.selectedId == null) {
            _events.postValue(Event(TimeRangeReportEvent.InputNotValid))
            return
        }
        val utcTimeFormat = SimpleDateFormat("yyyy-MM-dd");
        utcTimeFormat.timeZone = TimeZone.getTimeZone("UTC")


        val selectedId = state.selectedId
        require(selectedId != null) { "Account not selected" }

        val start = utcTimeFormat.format(state.startTime)
        val end = utcTimeFormat.format(state.endTime)


        viewModelScope.launch {
            transactionsRepository.getTimeRangeReport(
                start,
                end,
                selectedId
            ).fold({
                Timber.e(it)
                _events.postValue(Event(TimeRangeReportEvent.NetworkError))
            }, { result ->
                Timber.d("Generate daily report success $result")
                state = state.copy(results = result)
            })
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun map() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val startTime =
            if (state.startTime != null) dateFormat.format(state.startTime) else null

        val endTime =
            if (state.endTime != null) dateFormat.format(state.endTime) else null


        _props.postValue(
            TimeRangeReportFragment.Props(
                startTime,
                endTime,
                state.accounts.map { it.id to it.title },
                state.results.map {
                    TimeRangeReportFragment.Props.DailySpent(
                        it.id.createdAt,
                        it.totalSpent,
                        it.count
                    )
                }
            )
        )
    }
}