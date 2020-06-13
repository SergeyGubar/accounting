package io.github.gubarsergey.accounting.ui.prediction

import androidx.lifecycle.viewModelScope
import io.github.gubarsergey.accounting.data.account.Account
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import io.github.gubarsergey.accounting.ui.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

sealed class PredictionsIntent {
    data class FetchInfo(val accountId: String) : PredictionsIntent()
    object LoadAccounts : PredictionsIntent()
    data class SpinnerItemClicked(val position: Int) : PredictionsIntent()
}

sealed class PredictionsEvent {

}

data class PredictionsState(
    val accountId: String? = null,
    val accounts: List<Account> = emptyList(),
    val predictions: List<Prediction> = emptyList(),
    val formula: String = "",
    val coef: String = "",
    val points: List<List<Double>> = listOf()

)


class PredictionsInteractor(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) :
    BaseViewModel<PredictionsIntent, PredictionsEvent, PredictionFragment.Props, PredictionsState>(
        PredictionsState()
    ) {

    override fun handle(intent: PredictionsIntent) {
        when (intent) {
            is PredictionsIntent.FetchInfo -> {
                viewModelScope.launch {
                    transactionsRepository.getPredictions(state.accountId!!)
                }
            }
            PredictionsIntent.LoadAccounts -> {
                viewModelScope.launch {
                    accountsRepository.loadMyAccounts().fold({
                        state = state.copy(accounts = it)
                    }, {
                        Timber.e("Network error $it")
                    })
                }
            }
            is PredictionsIntent.SpinnerItemClicked -> {
                viewModelScope.launch {
                    transactionsRepository.getPredictions(state.accounts[intent.position].id).fold({
                        Timber.e("Network error $it")
                    }, {
                        state = state.copy(
                            predictions = it.predictions,
                            formula = it.formula,
                            coef = it.coef.toString(),
                            points = it.points
                        )
                        Timber.d(it.toString())
                    })
                }
            }
        }
    }

    override fun map(state: PredictionsState): PredictionFragment.Props {

        val points = state.points.flatten()
        return PredictionFragment.Props(
            state.accounts.map { it.title },
            state.predictions.map {
                PredictionFragment.Props.Prediction(
                    it.month,
                    it.prediction[1]
                )
            }, state.formula, state.coef,
            "[${points.map { it.toString() }
                .foldIndexed(
                    "",
                    { index, a, b -> a + " " + b + if (index == points.lastIndex) "" else "," })}]"
        )
    }
}