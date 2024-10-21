package com.example.githubviewer.domain.model

data class RepoDetails(
    val id: Long,
    val name: String,
    val url: String,
    val licenseName: String,
    var forksCount: Int,
    var stargazersCount: Int,
    var watchersCount: Int,
)