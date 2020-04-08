package io.github.gubarsergey.accounting.data.account

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface AccountsApi {
    @GET("accounts/allMy")
    suspend fun allMyAccounts(): List<Account>
}

data class Account(
    @SerializedName("_id") val id: String,
    val title: String,
    val currentAmount: Int,
    val ownerId: String,
    val type: String
)

data class LoginResponse(
    @SerializedName("access_token") val token: String
)