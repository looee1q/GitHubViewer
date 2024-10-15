package com.example.githubviewer.repository.auth

sealed interface AuthScreenAction {

    data class ShowError(val message: String) : AuthScreenAction

    data object RouteToMain : AuthScreenAction
}