package io.github.gubarsergey.accounting.data.transaction

import com.google.gson.annotations.SerializedName
import io.github.gubarsergey.accounting.data.category.CategoryDto
import retrofit2.http.*


interface TransactionsApi {
    @GET("transactions/{id}")
    suspend fun allTransactions(@Path("id") id: String): List<TransactionDto>

    @POST("transactions")
    suspend fun addTransaction(@Body dto: CreateTransactionDto): TransactionDto

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: String): TransactionDto

    @GET("transactions/category/categoryTotalSpent")
    suspend fun getGategoriesTotalSpent(): List<CategoryTotalSpentDto>
}

data class TransactionDto(
    @SerializedName("_id") val id: String,
    val amount: Int,
    val currentAmount: Double,
    val category: CategoryDto,
    val message: String
)

data class CreateTransactionDto(
    val accountId: String,
    val amount: Int,
    val categoryId: String,
    val message: String
)

data class CategoryTotalSpentDto(
    @SerializedName("_id") val id: CategoryTotalId,
    val totalAmount: Double,
    val count: Int
) {
    data class CategoryTotalId(
        val categoryId: String,
        val name: String
    )
}