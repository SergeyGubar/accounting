package io.github.gubarsergey.accounting.data.category

import arrow.core.Either
import io.github.gubarsergey.accounting.errors.NetworkError
import io.github.gubarsergey.accounting.errors.networkErrorMapper
import timber.log.Timber

class CategoryRepository(
    private val api: CategoryApi
) {

    suspend fun getMyCategories(): Either<List<CategoryDto>, NetworkError> {
        return try {
            val categories = api.allMy()
            Either.left(categories)
        } catch (ex: Exception) {
            Timber.e(ex, "Load accounts error ")
            Either.right(networkErrorMapper(ex))
        }
    }
}