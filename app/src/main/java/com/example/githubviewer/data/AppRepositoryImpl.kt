package com.example.githubviewer.data

import android.net.ConnectivityManager
import android.util.Log
import com.example.githubviewer.data.model.RepositoryInfoDto
import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.data.util.isDeviceConnectedToNetwork
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.NetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.RepositoryInfo
import com.example.githubviewer.domain.model.UserAuthStatus
import com.example.githubviewer.domain.model.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val connectivityManager: ConnectivityManager,
    private val userInfoMapper: Mapper<UserInfoDto, UserInfo>,
    private val repositoryInfoMapper: Mapper<RepositoryInfoDto, RepositoryInfo>,
) : AppRepository {

    private var bearerToken: String = ""

    override suspend fun getRepositories(): NetworkRequestResult<List<RepositoryInfo>> {
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
                .map { repositoryInfoMapper.map(it) }
            NetworkRequestResult.Success(userRepositories)
        } catch (e: Exception) {
            Log.d("AppRepository", "$e с описанием: ${e.message.toString()}")
            NetworkRequestResult.Error(NetworkError.OtherError(e.message.toString()))
        }
    }

    override suspend fun singIn(token: String): UserAuthStatus {
        bearerToken = TOKEN_PREFIX + token
        return try {
            val userInfoDto = apiService.authenticateUser(bearerToken)
            UserAuthStatus.Authorized(userInfoMapper.map(userInfoDto))
        } catch (e: Exception) {
            UserAuthStatus.NotAuthorized(e.message.toString())
        }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}