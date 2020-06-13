package io.github.gubarsergey.accounting.data.transaction

import arrow.core.Either
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import io.github.gubarsergey.accounting.ui.prediction.PredictionsDto
import timber.log.Timber

class TransactionsRepository(
    private val api: TransactionsApi
) {
    suspend fun addTransaction(
        accountId: String,
        amount: Int,
        categoryId: String,
        message: String
    ): Either<TransactionDto, NetworkError> {
        return try {
            val transaction =
                api.addTransaction(CreateTransactionDto(accountId, amount, categoryId, message))
            Either.left(transaction)
        } catch (ex: Exception) {
            Timber.e(ex, "Load accounts error ")
            Either.right(networkErrorMapper(ex))
        }
    }

    suspend fun getCategoryTotalSpent(): Either<Throwable, List<CategoryTotalSpentDto>> =
        Either.catch {
            api.getGategoriesTotalSpent()
        }

    suspend fun getTimeRangeReport(
        start: String,
        end: String,
        accountId: String
    ): Either<Throwable, TimeRangeReport> {
        return Either.catch {
            api.getTimeRangeReport(
                GenerateTimeRangeReportDto(
                    start,
                    end, accountId
                )
            )
        }
    }

    suspend fun getTotalReport(): Either<Throwable, List<TotalReport?>> {
        return Either.catch {
            api.getTotalReport()
        }
    }

    suspend fun getPredictions(accountId: String): Either<Throwable, PredictionsDto> {
        return Either.catch {
            api.getPredictions(accountId)
        }
    }
}