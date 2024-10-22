package com.example.githubviewer.repository.repositorydetails

import com.example.githubviewer.domain.model.RepoDetails

sealed interface DetailInfoScreenState {

    data object Initial : DetailInfoScreenState

    data object Loading : DetailInfoScreenState

    data class Loaded(
        val repoDetails: RepoDetails,
        val readmeState: ReadmeState
    ) : DetailInfoScreenState

    data class ErrorOther(val error: String) : DetailInfoScreenState

    data object ErrorNoConnection : DetailInfoScreenState
}