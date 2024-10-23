package com.example.githubviewer.domain.model

sealed interface UserAuthStatus {

    data class Authorized(val userInfo: UserInfo) : UserAuthStatus

    data class NotAuthorized(val baseNetworkError: BaseNetworkError) : UserAuthStatus
}