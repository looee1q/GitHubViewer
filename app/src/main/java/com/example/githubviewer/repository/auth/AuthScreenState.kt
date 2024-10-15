package com.example.githubviewer.repository.auth

sealed interface AuthScreenState {

    data object Initial : AuthScreenState

    data object Idle : AuthScreenState

    data object Loading : AuthScreenState

    data class InvalidInput(val reason: String) : AuthScreenState
}