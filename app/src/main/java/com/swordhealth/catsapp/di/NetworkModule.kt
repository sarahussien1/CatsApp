package com.swordhealth.catsapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swordhealth.catsapp.BuildConfig
import com.swordhealth.catsapp.services.CatsApiService
import com.swordhealth.catsapp.services.FavoritesApiService
import com.swordhealth.catsapp.utils.Constants
import com.swordhealth.catsapp.utils.Network
import com.swordhealth.catsapp.utils.NetworkConnectivity
import com.swordhealth.catsapp.utils.RetrofitInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Serialize Objects
            .build()
    }
    @Singleton
    @Provides
    fun provideOkHttpInterceptors(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideRetrofitInterceptor(): RetrofitInterceptor {
        return RetrofitInterceptor()
    }

    @Singleton
    @Provides
    fun okHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        retrofitInterceptor: RetrofitInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(retrofitInterceptor)
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideGsonFactory(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Singleton
    @Provides
    fun provideCatsApi(retrofit: Retrofit) =
        retrofit.create(CatsApiService::class.java)

    @Singleton
    @Provides
    fun provideFavoritesApi(retrofit: Retrofit) =
        retrofit.create(FavoritesApiService::class.java)
}