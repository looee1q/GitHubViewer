package com.example.githubviewer.data

import com.example.githubviewer.data.model.RepositoryInfoDto
import com.example.githubviewer.data.model.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("user")
    suspend fun authenticateUser(
        @Header("Authorization") personalAccessToken: String
    ) : UserInfoDto

    @GET("users/{username}/repos")
    suspend fun getListRepositoriesForUser(
        @Header("Authorization") personalAccessToken: String,
        @Path("username") username: String,
        @Query("per_page") perPage: Int
    ) : List<RepositoryInfoDto>
}