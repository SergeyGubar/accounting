package io.github.gubarsergey.accounting.data.account

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountsApi {
    @GET("accounts/allMy")
    suspend fun allMyAccounts(): List<Account>

    @POST("accounts")
    suspend fun addAccount(@Body dto: CreateAccountDto): Account

    @POST("accounts/remaining")
    suspend fun changeRemaining(@Body dto: ChangeRemainingDto): Account
}

data class ChangeRemainingDto(
    val id: String,
    val amount: Int
)

data class CreateAccountDto(
    val title: String,
    val currency: String,
    val type: String,
    val currentAmount: Int
)

data class Account(
    @SerializedName("_id") val id: String,
    val title: String,
    val currentAmount: Double,
    val ownerId: String,
    val type: String
)