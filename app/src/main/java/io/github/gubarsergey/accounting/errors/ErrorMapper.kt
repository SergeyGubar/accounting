package io.github.gubarsergey.accounting.errors

import androidx.annotation.StringRes
import io.github.gubarsergey.accounting.R
import retrofit2.HttpException
import java.net.ConnectException

sealed class NetworkError(@StringRes val message: Int) {
    object ConnectionTimeout : NetworkError(R.string.error_connection_timeout)
    object Unauthorized : NetworkError(R.string.error_unauthorized)
    object NotFound : NetworkError(R.string.error_not_found)
    object BadRequest : NetworkError(R.string.error_bad_request)
    object Generic : NetworkError(R.string.error_generic)
}

fun networkErrorMapper(
    ex: Throwable
): NetworkError {
    return when (ex) {
        is HttpException -> {
            when (ex.code()) {
                401 -> NetworkError.Unauthorized
                404 -> NetworkError.NotFound
                501, 502, 503 -> NetworkError.BadRequest
                else -> NetworkError.Generic
            }
        }
        is ConnectException -> NetworkError.ConnectionTimeout
        else -> NetworkError.Generic
    }
}