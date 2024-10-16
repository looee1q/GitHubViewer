package com.example.githubviewer.domain.model

data class RepositoryInfo(
    val id: Long,
    val name: String,
    val description: String,
    val language: String
)