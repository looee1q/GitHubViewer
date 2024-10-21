package com.example.githubviewer.domain

import com.example.githubviewer.domain.model.NetworkRequestResult
import com.example.githubviewer.domain.model.Repo
import com.example.githubviewer.domain.model.RepoDetails
import com.example.githubviewer.domain.model.UserAuthStatus

interface AppRepository {

    suspend fun getRepositories(): NetworkRequestResult<List<Repo>>

    suspend fun getRepository(repositoryName: String): NetworkRequestResult<RepoDetails>

    //suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): NetworkRequestResult<String>

    suspend fun singIn(token: String): UserAuthStatus

    suspend fun getUserAuthStatus(): UserAuthStatus
}