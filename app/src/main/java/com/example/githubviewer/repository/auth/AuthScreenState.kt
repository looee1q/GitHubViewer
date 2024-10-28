package com.example.githubviewer.repository.auth

sealed interface AuthScreenState {

    data object Initial : AuthScreenState

    data object AuthSuccess : AuthScreenState

    data object Loading : AuthScreenState

    data object NoConnection : AuthScreenState

    data class InvalidInput(val reason: String) : AuthScreenState
}