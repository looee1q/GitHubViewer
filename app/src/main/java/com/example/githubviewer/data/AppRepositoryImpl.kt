package com.example.githubviewer.data

import android.net.ConnectivityManager
import android.util.Log
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.data.util.isDeviceConnectedToNetwork
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.NetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.Repo
import com.example.githubviewer.domain.model.UserAuthStatus
import com.example.githubviewer.domain.model.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val keyValueStorage: KeyValueStorage,
    private val connectivityManager: ConnectivityManager,
    private val userInfoMapper: Mapper<UserInfoDto, UserInfo>,
    private val repoMapper: Mapper<RepoDto, Repo>,
) : AppRepository {

    private val bearerToken: String get() = keyValueStorage.getKey()

    override suspend fun getRepositories(): NetworkRequestResult<List<Repo>> {
        if (!connectivityManager.isDeviceConnectedToNetwork()) {
            return NetworkRequestResult.Error(NetworkError.NoConnection)
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
            NetworkRequestResult.Error(NetworkError.OtherError(e.message.toString()))
        }
    }

    override suspend fun singIn(token: String): UserAuthStatus {
        val bearerToken = TOKEN_PREFIX + token
        return try {
            val userInfoDto = apiService.authenticateUser(bearerToken)
            keyValueStorage.saveKey(token)
            UserAuthStatus.Authorized(userInfoMapper.map(userInfoDto))
        } catch (e: Exception) {
            UserAuthStatus.NotAuthorized(e.message.toString())
        }
    }

    override suspend fun getUserAuthStatus(): UserAuthStatus {
        Log.d("AppRepositoryImpl", "bearer token is $bearerToken")
        return if (bearerToken.isNotEmpty()) {
            singIn(bearerToken)
        } else {
            UserAuthStatus.NotAuthorized("")
        }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}