package io.github.gubarsergey.accounting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.data.user.UserRemoteDataSource
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.navigation.NavigationOperator
import io.github.gubarsergey.accounting.navigation.NavProps
import io.github.gubarsergey.accounting.navigation.NavigationConnector
import io.github.gubarsergey.accounting.navigation.Router
import io.github.gubarsergey.accounting.redux.AppReducer
import io.github.gubarsergey.accounting.redux.AppState
import io.github.gubarsergey.accounting.redux.Store
import io.github.gubarsergey.accounting.redux.connect
import io.github.gubarsergey.accounting.ui.login.LoginConnector
import io.github.gubarsergey.accounting.ui.login.LoginFragment
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

        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        // TODO: Move to env
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://34.102.228.226/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val usersModule = module {
            val props = MutableLiveData<LoginFragment.Props>()
            val userRemoteDataSource = UserRemoteDataSource(retrofit.create(UserApi::class.java))
            val userRepository = UserRepository(userRemoteDataSource)

            LoginConnector(userRepository).also { connector ->
                connector.connect(
                    store,
                    props.asConsumer()
                )
            }
            single<LiveData<LoginFragment.Props>> { props }
        }

        val navModule = module {
            single { (router: Router) ->
                NavigationOperator(NavProps.LOGIN, router).also { operator ->
                    NavigationConnector().connect(store, operator.asConsumer())
                }
            }
        }


        startKoin {
            androidContext(this@App)
            modules(listOf(usersModule, navModule))
        }
    }
}