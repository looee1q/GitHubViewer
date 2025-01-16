package com.example.githubviewer.data.apiservice

import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.data.model.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.appendPathSegments
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : GitHubApiService {

    override suspend fun authenticateUser(): UserInfoDto {
        return client.get {
            url(HttpRoutes.USER)
        }.body<UserInfoDto>()
    }

    override suspend fun getListRepositoriesForAuthenticatedUser(
        perPage: Int,
        sort: String
    ): List<RepoDto> {
        return client.get {
            url(HttpRoutes.USER_REPOS)
            parameter(key = HttpQueries.PER_PAGE, value = perPage)
            parameter(key = HttpQueries.SORT, value = sort)
        }.body<List<RepoDto>>()
    }

    override suspend fun getRepositoryDetails(
        repositoryOwner: String,
        repositoryName: String
    ): RepoDetailsDto {
        return client.get {
            url {
                appendPathSegments(
                    HttpRoutes.REPOS,
                    repositoryOwner,
                    repositoryName
                )
            }
        }.body<RepoDetailsDto>()
    }

    override suspend fun getRepositoryReadme(
        repositoryOwner: String,
        repositoryName: String
    ): RepoReadmeDto {
        return client.get {
            url {
                appendPathSegments(
                    HttpRoutes.REPOS,
                    repositoryOwner,
                    repositoryName,
                    HttpRoutes.README
                )
            }
        }.body<RepoReadmeDto>()
    }
}