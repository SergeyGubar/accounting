package io.github.gubarsergey.accounting.data.account

import arrow.core.Either
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import timber.log.Timber

class AccountsRepository(
    private val api: AccountsApi
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
}