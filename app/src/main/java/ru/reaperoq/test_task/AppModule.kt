package ru.reaperoq.test_task

import android.content.Context
import com.google.gson.GsonBuilder
import com.liftric.kvault.KVault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.reaperoq.test_task.datasource.MainDataSource
import ru.reaperoq.test_task.datasource.MainService
import ru.reaperoq.test_task.datasource.models.Payment
import ru.reaperoq.test_task.datasource.models.deserializer.PaymentDeserializer
import ru.reaperoq.test_task.repository.MainRepository
import ru.reaperoq.test_task.repository.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideVault(@ApplicationContext context: Context) = KVault(context)

    @Provides
    fun provideBaseUrl() = "https://easypay.world/"

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("app-key", "12345")
                .addHeader("v", "1")
                .build()
            chain.proceed(request)
        }
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonBuilder()
        .registerTypeAdapter(Payment::class.java, PaymentDeserializer())
        .create()
        .let { GsonConverterFactory.create(it) }

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String, client: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService = retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideMainDataSource(mainService: MainService, kVault: KVault): MainDataSource = MainDataSource(kVault, mainService)

    @Provides
    @Singleton
    fun provideMainRepository(mainDataSource: MainDataSource): MainRepository = MainRepositoryImpl(mainDataSource)
}