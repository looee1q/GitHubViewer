package com.example.githubviewer.di

import com.example.githubviewer.data.KeyValueStorage
import com.example.githubviewer.data.apiservice.GitHubApiService
import com.example.githubviewer.data.apiservice.GitHubApiServiceImpl
import com.example.githubviewer.data.apiservice.HttpRoutes
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiKtorModule {

    @Singleton
    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Singleton
    @Provides
    fun provideOkHttpEngine() = OkHttp.create()

    @Singleton
    @Provides
    fun providesHttpClient(
        engine: HttpClientEngine,
        json: Json,
        keyValueStorage: KeyValueStorage
    ): HttpClient {

        return HttpClient(engine) {

            expectSuccess = true

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(json = json)
            }

            install(DefaultRequest) {
                url(HttpRoutes.BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, keyValueStorage.getKey())
            }
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiServiceModule() {

    @Singleton
    @Binds
    abstract fun bindApiService(impl: GitHubApiServiceImpl): GitHubApiService
}