package com.example.githubviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    @SerialName("id") val id: Long,
    @SerialName("login") val login: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
)
