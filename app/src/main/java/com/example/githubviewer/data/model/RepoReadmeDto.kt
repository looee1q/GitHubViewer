package com.example.githubviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoReadmeDto(
    @SerialName("content") val content: String?
)
