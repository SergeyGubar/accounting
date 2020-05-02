package io.github.gubarsergey.accounting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.gubarsergey.accounting.data.TokenInterceptor
import io.github.gubarsergey.accounting.data.account.AccountsApi
import io.github.gubarsergey.accounting.data.account.AccountsRepository
import io.github.gubarsergey.accounting.data.category.CategoryApi
import io.github.gubarsergey.accounting.data.category.CategoryRepository
import io.github.gubarsergey.accounting.data.transaction.TransactionsApi
import io.github.gubarsergey.accounting.data.transaction.TransactionsRepository
import io.github.gubarsergey.accounting.data.user.UserApi
import io.github.gubarsergey.accounting.data.user.UserRepository
import io.github.gubarsergey.accounting.navigation.NavProps
import io.github.gubarsergey.accounting.navigation.NavigationConnector
import io.github.gubarsergey.accounting.navigation.NavigationOperator
import io.github.gubarsergey.accounting.navigation.Router
import io.github.gubarsergey.accounting.operator.SharedPrefConnector
import io.github.gubarsergey.accounting.operator.SharedPrefOperator
import io.github.gubarsergey.accounting.operator.asConsumer
import io.github.gubarsergey.accounting.redux.*
import io.github.gubarsergey.accounting.ui.account.AddAccountInteractor
import io.github.gubarsergey.accounting.ui.category.add.AddCategoryInteractor
import io.github.gubarsergey.accounting.ui.category.list.CategoryListInteractor
import io.github.gubarsergey.accounting.ui.category.total.CategoryTotalSpentInteractor
import io.github.gubarsergey.accounting.ui.login.LoginConnector
import io.github.gubarsergey.accounting.ui.login.LoginFragment
import io.github.gubarsergey.accounting.ui.report.AllTimeReportInteractor
import io.github.gubarsergey.accounting.ui.transaction.AddTransactionsInteractor
import io.github.gubarsergey.accounting.ui.transaction.list.AccountsInteractor
import io.github.gubarsergey.accounting.ui.transaction.report.TimeRangeReportInteractor
import io.github.gubarsergey.accounting.util.SharedPrefHelper
import io.github.gubarsergey.accounting.util.asConsumer
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

    private val store = Store(
        AppState(),
        AppReducer
    )

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        val prefHelper = SharedPrefHelper()

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val tokenInterceptor = TokenInterceptor {
            // TODO: Extract token once and use afterwards
            prefHelper.getToken(applicationContext)
        }

        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

        // TODO: Move to env
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://34.102.228.226/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val networkModule = module {
            val accountsApi = retrofit.create(AccountsApi::class.java)
            val transactionsApi = retrofit.create(TransactionsApi::class.java)
            val categoryApi = retrofit.create(CategoryApi::class.java)
            single {
                AccountsRepository(accountsApi, transactionsApi)
            }
            single {
                CategoryRepository(categoryApi)
            }
            single {
                TransactionsRepository(transactionsApi)
            }
        }

        val usersModule = module {
            val props = MutableLiveData<LoginFragment.Props>()
            val userApi = retrofit.create(UserApi::class.java)

            val userRepository =
                UserRepository(userApi)

            LoginConnector(userRepository).also { connector ->
                connector.connect(
                    store,
                    props.asConsumer()
                )
            }
            single<LiveData<LoginFragment.Props>> { props }
        }

        val accountsModule = module {
            factory {
                AccountsInteractor(
                    get(),
                    MutableLiveData()
                )
            }
        }

        val operatorsModule = module {
            single {
                SharedPrefOperator(get(), prefHelper).also { operator ->
                    SharedPrefConnector().connect(store, operator.asConsumer)
                }
            }
            single { (router: Router) ->
                NavigationOperator(NavProps.LOGIN, router).also { operator ->
                    NavigationConnector().connect(store, operator.asConsumer())
                }
            }
        }

        val addTransactionModule = module {
            factory {
                AddTransactionsInteractor(get(), get(), get(), MutableLiveData(), MutableLiveData())
            }
        }

        val addAccountModule = module {
            factory {
                AddAccountInteractor(get(), MutableLiveData())
            }
        }

        val addCategoryModule = module {
            factory {
                AddCategoryInteractor(
                    get(),
                    MutableLiveData()
                )
            }
        }

        val categoryListModule = module {
            viewModel {
                CategoryListInteractor(
                    get(),
                    MutableLiveData()
                )
            }
        }

        val reportsModule = module {
            viewModel {
                CategoryTotalSpentInteractor(
                    get(),
                    MutableLiveData(),
                    MutableLiveData()
                )
            }
            viewModel {
                TimeRangeReportInteractor(
                    get(),
                    get(),
                    MutableLiveData(),
                    MutableLiveData()
                )
            }
            viewModel {
                AllTimeReportInteractor(get())
            }
        }

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    usersModule,
                    networkModule,
                    operatorsModule,
                    accountsModule,
                    addTransactionModule,
                    addAccountModule,
                    addCategoryModule,
                    categoryListModule,
                    reportsModule
                )
            )
        }
    }
}