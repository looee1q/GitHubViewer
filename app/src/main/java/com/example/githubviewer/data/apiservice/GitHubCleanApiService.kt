package com.example.githubviewer.data.apiservice

import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.data.model.UserInfoDto

interface GitHubCleanApiService {

    suspend fun authenticateUser(): UserInfoDto

    suspend fun getListRepositoriesForAuthenticatedUser(
        perPage: Int,
        sort: String
    ): List<RepoDto>

    suspend fun getRepositoryDetails(
        repositoryOwner: String,
        repositoryName: String,
    ): RepoDetailsDto

    suspend fun getRepositoryReadme(
        repositoryOwner: String,
        repositoryName: String,
    ): RepoReadmeDto
}