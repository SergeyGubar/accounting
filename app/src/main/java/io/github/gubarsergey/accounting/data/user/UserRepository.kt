package io.github.gubarsergey.accounting.data.user

import arrow.core.Either
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import timber.log.Timber
import java.lang.Exception


class UserRepository(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun login(credentials: Credentials): Either<String, NetworkError> {
        return try {
            val result = remoteDataSource.loginAsync(credentials)
            Either.left(result.token)
        } catch (ex: Exception) {
            Timber.e(ex)
            Either.right(networkErrorMapper(ex))
        }
    }
}