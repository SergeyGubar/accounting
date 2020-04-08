package io.github.gubarsergey.accounting.data.user

import arrow.core.Either
import io.github.gubarsergey.accounting.data.user.Credentials
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import timber.log.Timber
import java.lang.Exception


class UserRepository(
    private val userApi: UserApi
) {
    suspend fun login(credentials: Credentials): Either<String, NetworkError> {
        Timber.d("Login started")
        return try {
            val result = userApi.loginAsync(credentials)
            Timber.d("Login finished successfully, token: ${result.token}")
            Either.left(result.token)

        } catch (ex: Exception) {
            Timber.e(ex, "Login error")
            Either.right(networkErrorMapper(ex))
        }
    }
}