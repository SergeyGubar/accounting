package io.github.gubarsergey.accounting.data.category

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class CategoryDto(
    @SerializedName("_id") val id: String,
    val ownerId: String,
    val title: String
)

data class CreateCategoryDto(
    val title: String
)

interface CategoryApi {
    @GET("categories/allMy")
    suspend fun allMy(): List<CategoryDto>

    @POST("categories/")
    suspend fun addCategory(@Body dto: CreateCategoryDto): CategoryDto
}