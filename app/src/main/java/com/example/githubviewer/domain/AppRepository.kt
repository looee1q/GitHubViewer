package com.example.githubviewer.domain

import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.RepositoryInfo
import com.example.githubviewer.domain.model.UserAuthStatus

interface AppRepository {

    suspend fun getRepositories(): NetworkRequestResult<List<RepositoryInfo>>

    //suspend fun getRepository(repoId: String): NetworkRequestResult<List<Repository>>

    //suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): NetworkRequestResult<String>

    suspend fun singIn(token: String): UserAuthStatus
}