package io.github.gubarsergey.accounting.data.category

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class CategoryDto(
    @SerializedName("_id") val id: String,
    val ownerId: String,
    val title: String
)

interface CategoryApi {
    @GET("categories/allMy")
    suspend fun allMy(): List<CategoryDto>
}