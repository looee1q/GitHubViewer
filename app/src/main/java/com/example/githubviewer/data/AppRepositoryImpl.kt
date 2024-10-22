package com.example.githubviewer.data

import android.net.ConnectivityManager
import android.util.Log
import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.data.util.isDeviceConnectedToNetwork
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.BaseNetworkError
import com.example.githubviewer.domain.model.ExtendedNetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.Repo
import com.example.githubviewer.domain.model.RepoDetails
import com.example.githubviewer.domain.model.RepoReadme
import com.example.githubviewer.domain.model.UserAuthStatus
import com.example.githubviewer.domain.model.UserInfo
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val keyValueStorage: KeyValueStorage,
    private val connectivityManager: ConnectivityManager,
    private val userInfoMapper: Mapper<UserInfoDto, UserInfo>,
    private val repoMapper: Mapper<RepoDto, Repo>,
    private val repoDetailsMapper: Mapper<RepoDetailsDto, RepoDetails>,
    private val repoReadmeMapper: Mapper<RepoReadmeDto, RepoReadme>,
) : AppRepository {

    private val bearerToken: String get() = keyValueStorage.getKey()

    override suspend fun getRepositories(): NetworkRequestResult<List<Repo>, BaseNetworkError> {
        if (!connectivityManager.isDeviceConnectedToNetwork()) {
            return NetworkRequestResult.Error(BaseNetworkError.NoConnection)
        }
        return try {
            val userRepositories = apiService
                .getListRepositoriesForUser(
                    personalAccessToken = bearerToken,
                    perPage = 10,
                    username = "looee1q"
                )
                .map { repoMapper.map(it) }
            NetworkRequestResult.Success(userRepositories)
        } catch (e: Exception) {
            Log.d("AppRepositoryImpl", "$e с описанием: ${e.message.toString()}")
            NetworkRequestResult.Error(BaseNetworkError.OtherError(e.message.toString()))
        }
    }

    override suspend fun getRepository(
        repositoryName: String
    ): NetworkRequestResult<RepoDetails, BaseNetworkError> {
        if (!connectivityManager.isDeviceConnectedToNetwork()) {
            return NetworkRequestResult.Error(BaseNetworkError.NoConnection)
        }
        return try {
            val repositoryDetails = apiService
                .getRepositoryDetails(
                    personalAccessToken = bearerToken,
                    repositoryOwner = "looee1q",
                    repositoryName = repositoryName
                ).run {
                    repoDetailsMapper.map(this)
                }
            NetworkRequestResult.Success(repositoryDetails)
        } catch (e: Exception) {
            Log.d("AppRepositoryImpl", "$e с описанием: ${e.message.toString()}")
            NetworkRequestResult.Error(BaseNetworkError.OtherError(e.message.toString()))
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getRepositoryReadme(
        repositoryName: String
    ): NetworkRequestResult<RepoReadme, ExtendedNetworkError> {
        if (!connectivityManager.isDeviceConnectedToNetwork()) {
            return NetworkRequestResult.Error(ExtendedNetworkError.NoConnection)
        }
        return try {
            val repositoryReadme = apiService
                .getRepositoryReadme(
                    personalAccessToken = bearerToken,
                    repositoryOwner = "looee1q",
                    repositoryName = repositoryName
                ).run {
                    val encoded = this.copy(
                        content = String(Base64.Mime.decode(this.content.orEmpty()))
                    )
                    repoReadmeMapper.map(encoded)
                }
            NetworkRequestResult.Success(repositoryReadme)
        } catch (httpException: HttpException) {
            if (httpException.code() == 404) {
                NetworkRequestResult.Error(ExtendedNetworkError.ResourceNotFoundError)
            } else {
                NetworkRequestResult.Error(ExtendedNetworkError.OtherError(httpException.message.toString()))
            }
        } catch (e: Exception) {
            Log.d("AppRepositoryImpl", "$e с описанием: ${e.message.toString()}")
            NetworkRequestResult.Error(ExtendedNetworkError.OtherError(e.message.toString()))
        }
    }

    override suspend fun singIn(token: String): UserAuthStatus {
        val bearerToken = TOKEN_PREFIX + token
        return try {
            val userInfoDto = apiService.authenticateUser(bearerToken)
            keyValueStorage.saveKey(bearerToken)
            UserAuthStatus.Authorized(userInfoMapper.map(userInfoDto))
        } catch (e: Exception) {
            UserAuthStatus.NotAuthorized(e.message.toString())
        }
    }

    override suspend fun getUserAuthStatus(): UserAuthStatus {
        Log.d("AppRepositoryImpl", "bearer token is $bearerToken")
        return if (bearerToken.isNotEmpty()) {
            val token = bearerToken.removePrefix(TOKEN_PREFIX)
            singIn(token)
        } else {
            UserAuthStatus.NotAuthorized("")
        }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}