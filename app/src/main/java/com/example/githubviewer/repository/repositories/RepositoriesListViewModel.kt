package com.example.githubviewer.repository.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.data.AppRepository
import com.example.githubviewer.data.NetworkClient
import com.example.githubviewer.data.model.mappers.RepositoryInfoMapper
import com.example.githubviewer.data.model.mappers.UserInfoMapper
import com.example.githubviewer.domain.model.NetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoriesListViewModel : ViewModel() {
    private val repository = AppRepository(
        networkClient = NetworkClient(),
        userInfoMapper = UserInfoMapper(),
        repositoryInfoMapper = RepositoryInfoMapper()
    )

    private val _screenState = MutableStateFlow<RepositoriesListScreenState>(
        RepositoriesListScreenState.Initial
    )
    val screenState: StateFlow<RepositoriesListScreenState> = _screenState.asStateFlow()

    init {
        loadRepositories()
    }

    private fun loadRepositories() {
        _screenState.value = RepositoriesListScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val networkRequestResult = repository.getRepositories()) {
                is NetworkRequestResult.Error<*> -> {
                    when (networkRequestResult.error) {
                        NetworkError.NoConnection -> {
                            _screenState.value = RepositoriesListScreenState.ErrorNoConnection
                        }

                        is NetworkError.OtherError -> {
                            _screenState.value = RepositoriesListScreenState.ErrorOther(
                                networkRequestResult.error.message
                            )
                        }
                    }
                }

                is NetworkRequestResult.Success -> {
                    if (networkRequestResult.data.isEmpty()) {
                        _screenState.value = RepositoriesListScreenState.Empty
                    } else {
                        _screenState.value = RepositoriesListScreenState.Loaded(
                            networkRequestResult.data
                        )
                    }
                }
            }
        }
    }
}