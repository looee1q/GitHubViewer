package com.example.githubviewer.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val description: String,
    val language: String
)