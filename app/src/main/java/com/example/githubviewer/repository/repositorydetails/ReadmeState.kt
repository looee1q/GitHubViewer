package com.example.githubviewer.repository.repositorydetails


sealed interface ReadmeState {

    data object Initial : ReadmeState

    data object Loading : ReadmeState

    data class Loaded(val markdown: String) : ReadmeState

    data class ErrorOther(val error: String) : ReadmeState

    data object ErrorNoConnection : ReadmeState

    data object Empty : ReadmeState

    data object NotExists : ReadmeState
}