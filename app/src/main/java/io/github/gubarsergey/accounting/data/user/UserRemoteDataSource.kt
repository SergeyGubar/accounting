package io.github.gubarsergey.accounting.data.user

class UserRemoteDataSource(
    private val userApi: UserApi
) {
    suspend fun loginAsync(credentials: Credentials): LoginResponse = userApi.loginAsync(credentials)
}