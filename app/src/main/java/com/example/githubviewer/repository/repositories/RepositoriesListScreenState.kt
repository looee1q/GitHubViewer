package com.example.githubviewer.repository.repositories

import com.example.githubviewer.domain.model.Repo

sealed interface RepositoriesListScreenState {

    data object Initial : RepositoriesListScreenState

    data object Loading : RepositoriesListScreenState

    data class Loaded(val repos: List<Repo>) : RepositoriesListScreenState

    data class ErrorOther(val error: String) : RepositoriesListScreenState

    data object ErrorNoConnection : RepositoriesListScreenState

    data object Empty : RepositoriesListScreenState
}