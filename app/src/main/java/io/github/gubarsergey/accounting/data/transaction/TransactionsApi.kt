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

    @POST("transactions/report/timeRange")
    suspend fun getTimeRangeReport(@Body dto: GenerateTimeRangeReportDto): TimeRangeReport

    @POST("transactions/report/total")
    suspend fun getTotalReport(): List<TotalReport?>
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

typealias TimeRangeReport = List<DailySpentReport>

data class GenerateTimeRangeReportDto(
    val startDate: String,
    val endDate: String,
    val accountId: String
)

data class DailySpentReport(
    @SerializedName("_id") val id: Id,
    val totalSpent: Double,
    val count: Int
) {
    data class Id(
        val month: Int,
        val day: Int,
        val year: Int,
        val createdAt: String
    )
}


data class TotalReport(
    @SerializedName("_id") val id: Id,
    val totalEarned: Int,
    val countEarned: Int,
    val totalSpent: Int,
    val countSpent: Int
) {
    data class Id(
        val id: String,
        val title: String
    )
}