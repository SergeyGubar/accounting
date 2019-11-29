package io.github.gubarsergey.accounting.data.user

import retrofit2.http.Body
import retrofit2.http.POST

data class Credentials(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

interface UserApi {
    @POST("login")
    suspend fun loginAsync(@Body credentials: Credentials): LoginResponse
}