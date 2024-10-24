package com.example.githubviewer.data

import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.data.model.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("user")
    suspend fun authenticateUser(): UserInfoDto

    @GET("users/{username}/repos")
    suspend fun getListRepositoriesForUser(
        @Path("username") username: String,
        @Query("per_page") perPage: Int
    ): List<RepoDto>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") repositoryOwner: String,
        @Path("repo") repositoryName: String,
    ): RepoDetailsDto

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getRepositoryReadme(
        @Path("owner") repositoryOwner: String,
        @Path("repo") repositoryName: String,
    ): RepoReadmeDto
}