package io.github.gubarsergey.accounting

import android.app.Application
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.data.user.UserRemoteDataSource
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.ui.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val networkmodule = module {
            single {
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
            }

            single {
                Retrofit.Builder()
                    .client(get())
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }

        val usersModule = module {
            viewModel {
                LoginViewModel(get())
            }
            single {
                UserRemoteDataSource(get<Retrofit>().create(UserApi::class.java))
            }
            single {
                UserRepository(get())
            }
        }


        startKoin {
            androidContext(this@App)
            modules(listOf(networkmodule, usersModule))
        }
    }
}