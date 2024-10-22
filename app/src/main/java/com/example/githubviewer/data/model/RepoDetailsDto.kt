package com.example.githubviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("html_url") val url: String,
    @SerialName("license") val licenseDto: LicenseDto?,
    @SerialName("forks_count") var forksCount: Int,
    @SerialName("stargazers_count") var stargazersCount: Int,
    @SerialName("watchers_count") var watchersCount: Int,
)

@Serializable
data class LicenseDto(
    @SerialName("spdx_id") val name: String,
)
