package com.example.githubviewer.data

import com.example.githubviewer.data.model.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Header

interface GitHubService {

    @GET("user")
    suspend fun authenticateUser(
        @Header("Authorization") personalAccessToken: String
    ) : UserInfoDto
}