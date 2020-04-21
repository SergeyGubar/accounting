package io.github.gubarsergey.accounting.data.category

import arrow.core.Either
import arrow.core.extensions.either.monad.flatMap
import io.github.gubarsergey.accounting.data.transaction.CategoryTotalSpentDto
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

    suspend fun addCategory(dto: CreateCategoryDto): Either<Throwable, CategoryDto> {
        return Either.catch {
            api.addCategory(dto)
        }
    }
}