package io.github.gubarsergey.accounting.data

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(private val tokenGetter: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()


        if (original.url.encodedPath.contains("/login") && original.method == "POST"
            || (original.url.encodedPath.contains("/sign-up") && original.method == "POST")
        ) {
            return chain.proceed(original)
        }

        val token = tokenGetter()

        require(token != null) { "Token cannot be null" }

        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}