package io.github.gubarsergey.accounting.data.transaction

import com.google.gson.annotations.SerializedName
import io.github.gubarsergey.accounting.data.category.CategoryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface TransactionsApi {
    @GET("transactions/{id}")
    suspend fun allTransactions(@Path("id") id: String): List<TransactionDto>

    @POST("transactions")
    suspend fun addTransaction(@Body dto: AddTransactionDto): TransactionDto
}

data class TransactionDto(
    @SerializedName("_id") val id: String,
    val amount: Int,
    val category: CategoryDto
)

data class AddTransactionDto(
    val accountId: String,
    val amount: Int,
    val categoryId: String
)