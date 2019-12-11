package io.github.gubarsergey.accounting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.data.user.UserRemoteDataSource
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.redux.AppReducer
import io.github.gubarsergey.accounting.redux.AppState
import io.github.gubarsergey.accounting.redux.Store
import io.github.gubarsergey.accounting.redux.connect
import io.github.gubarsergey.accounting.redux.login.LoginState
import io.github.gubarsergey.accounting.ui.login.LoginFragment
import io.github.gubarsergey.accounting.ui.login.LoginReduce
import io.github.gubarsergey.accounting.ui.login.LoginConnector
import io.github.gubarsergey.accounting.util.asConsumer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class App : Application() {

    private val store = Store(
        AppState(),
        AppReducer
    )


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val networkModule = module {
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
            val props = MutableLiveData<LoginFragment.Props>()
            single {
                LoginConnector(get()).also { connector ->
                    connector.connect(
                        store,
                        props.asConsumer()
                    )
                }
            }
            single<LiveData<LoginFragment.Props>> {
                props
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
            modules(listOf(networkModule, usersModule))
        }
    }
}