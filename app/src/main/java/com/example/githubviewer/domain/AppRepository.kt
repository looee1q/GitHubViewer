package com.example.githubviewer.domain

import com.example.githubviewer.domain.model.BaseNetworkError
import com.example.githubviewer.domain.model.ExtendedNetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.Repo
import com.example.githubviewer.domain.model.RepoDetails
import com.example.githubviewer.domain.model.RepoReadme
import com.example.githubviewer.domain.model.UserAuthStatus

interface AppRepository {

    suspend fun getRepositories(): NetworkRequestResult<List<Repo>, BaseNetworkError>

    suspend fun getRepository(
        repositoryName: String
    ): NetworkRequestResult<RepoDetails, BaseNetworkError>

    suspend fun getRepositoryReadme(
        repositoryName: String
    ): NetworkRequestResult<RepoReadme, ExtendedNetworkError>

    suspend fun singIn(token: String): UserAuthStatus

    suspend fun getUserAuthStatus(): UserAuthStatus
}