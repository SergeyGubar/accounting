package io.github.gubarsergey.accounting.data.user

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class Credentials(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token") val token: String
)

interface UserApi {
    @POST("users/login")
    suspend fun loginAsync(@Body credentials: Credentials): LoginResponse
}