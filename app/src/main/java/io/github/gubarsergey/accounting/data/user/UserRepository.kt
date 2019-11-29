package io.github.gubarsergey.accounting.data.user

import java.lang.Exception


class UserRepository(private val remoteDataSource: UserRemoteDataSource) {
    suspend fun login(credentials: Credentials): Result<String> {
        return try {
            val result = remoteDataSource.loginAsync(credentials)
            Result.success(result.token)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}