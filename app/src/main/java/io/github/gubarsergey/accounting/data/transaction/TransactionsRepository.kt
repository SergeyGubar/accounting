package io.github.gubarsergey.accounting.data.transaction

import arrow.core.Either
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
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
}