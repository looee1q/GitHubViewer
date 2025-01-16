package com.example.githubviewer.di

import com.example.githubviewer.data.KeyValueStorage
import com.example.githubviewer.data.apiservice.GitHubApiService
import com.example.githubviewer.data.apiservice.GitHubCleanApiService
import com.example.githubviewer.data.apiservice.HttpRoutes.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

/*
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpTokenHeaderInterceptor

@Module
@InstallIn(SingletonComponent::class)
object ApiRetrofitModule {

    private const val HEADER_AUTHORIZATION = "Authorization"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    @HttpTokenHeaderInterceptor
    fun provideHttpHeaderInterceptor(keyValueStorage: KeyValueStorage): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .headers(
                        chain.request().headers.newBuilder()
                            .addUnsafeNonAscii(HEADER_AUTHORIZATION, keyValueStorage.getKey())
                            .build()
                    )
                    .build()
            )
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @HttpTokenHeaderInterceptor httpTokenHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpTokenHeaderInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideJson() = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create<GitHubApiService>()

    @Singleton
    @Provides
    fun provideCleanApiService(
        retrofit: Retrofit
    ): GitHubCleanApiService = retrofit.create<GitHubApiService>()
}
*/