package io.github.gubarsergey.accounting.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Integrate DI

object ApiFactory {
    private val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}