package com.example.githubviewer.data

import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.domain.model.UserAuthStatus
import com.example.githubviewer.domain.model.UserInfo

class AppRepository(
    private val networkClient: NetworkClient,
    private val userInfoMapper: Mapper<UserInfoDto, UserInfo>
) {

    suspend fun singIn(token: String): UserAuthStatus {
        val bearerToken = TOKEN_PREFIX + token
        return try {
            val userInfoDto = networkClient.service.authenticateUser(bearerToken)
            UserAuthStatus.Authorized(userInfoMapper.map(userInfoDto))
        } catch (e: Exception) {
            UserAuthStatus.NotAuthorized(e.message.toString())
        }
    }

    companion object {
        private const val TOKEN_PREFIX = "Bearer "
    }
}