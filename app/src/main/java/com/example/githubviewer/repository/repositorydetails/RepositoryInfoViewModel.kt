package com.example.githubviewer.repository.repositorydetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.BaseNetworkError
import com.example.githubviewer.domain.model.ExtendedNetworkError
import com.example.githubviewer.domain.model.NetworkRequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow<DetailInfoScreenState>(
        DetailInfoScreenState.Initial
    )
    val screenState: StateFlow<DetailInfoScreenState> = _screenState.asStateFlow()

    private val repositoryName = savedStateHandle.get<String>(REPOSITORY_NAME).orEmpty()

    init {
        viewModelScope.launch {
            getRepository()
            getReadme()
        }
    }

    private suspend fun getRepository() {
        _screenState.value = DetailInfoScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val networkRequestResult = repository.getRepository(repositoryName)) {
                is NetworkRequestResult.Error<BaseNetworkError> -> {
                    when (networkRequestResult.error) {
                        BaseNetworkError.NoConnection -> {
                            _screenState.value = DetailInfoScreenState.ErrorNoConnection
                        }

                        is BaseNetworkError.OtherError -> {
                            _screenState.value = DetailInfoScreenState.ErrorOther(
                                error = networkRequestResult.error.message
                            )
                        }
                    }
                }

                is NetworkRequestResult.Success -> {
                    _screenState.value = DetailInfoScreenState.Loaded(
                        repoDetails = networkRequestResult.data,
                        readmeState = ReadmeState.Initial
                    )
                }
            }
        }.join()
    }

    private fun getReadme() {
        val currentScreenState = screenState.value
        if (currentScreenState is DetailInfoScreenState.Loaded) {
            _screenState.value = currentScreenState.copy(readmeState = ReadmeState.Loading)
            viewModelScope.launch(Dispatchers.IO) {
                when (val networkRequestResult = repository.getRepositoryReadme(repositoryName)) {
                    is NetworkRequestResult.Error<ExtendedNetworkError> -> {
                        when (networkRequestResult.error) {
                            ExtendedNetworkError.NoConnection -> {
                                _screenState.value = currentScreenState.copy(
                                    readmeState = ReadmeState.ErrorNoConnection
                                )
                            }

                            is ExtendedNetworkError.OtherError -> {
                                _screenState.value = currentScreenState.copy(
                                    readmeState = ReadmeState.ErrorOther(
                                        error = networkRequestResult.error.message
                                    )
                                )
                            }

                            ExtendedNetworkError.ResourceNotFoundError -> {
                                _screenState.value = currentScreenState.copy(
                                    readmeState = ReadmeState.NotExists
                                )
                            }
                        }
                    }

                    is NetworkRequestResult.Success -> {
                        if (networkRequestResult.data.content.isEmpty()) {
                            _screenState.value = currentScreenState.copy(
                                readmeState = ReadmeState.Empty
                            )
                        } else {
                            _screenState.value = currentScreenState.copy(
                                readmeState = ReadmeState.Loaded(networkRequestResult.data.content)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REPOSITORY_NAME = "REPOSITORY_NAME"
    }
}