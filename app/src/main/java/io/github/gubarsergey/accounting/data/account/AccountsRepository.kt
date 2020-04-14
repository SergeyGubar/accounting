package io.github.gubarsergey.accounting.data.account

import arrow.core.Either
import io.github.gubarsergey.accounting.data.transaction.TransactionDto
import io.github.gubarsergey.accounting.data.transaction.TransactionsApi
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import timber.log.Timber

class AccountsRepository(
    private val api: AccountsApi,
    private val transactionsApi: TransactionsApi
) {
    suspend fun loadMyAccounts(): Either<List<Account>, NetworkError> {
        return try {
            val accounts = api.allMyAccounts()
            Either.left(accounts)
        } catch (ex: Exception) {
            Timber.e(ex, "Load accounts error ")
            Either.right(networkErrorMapper(ex))
        }
    }

    suspend fun loadTransactions(id: String): Either<List<TransactionDto>, NetworkError> {
        return try {
            val accounts = transactionsApi.allTransactions(id)
            Either.left(accounts)
        } catch (ex: Exception) {
            Timber.e(ex, "Load accounts error ")
            Either.right(networkErrorMapper(ex))
        }
    }

    suspend fun addAccount(dto: CreateAccountDto): Either<Throwable, Account> {
        return Either.catch {
            api.addAccount(dto)
        }
    }
}