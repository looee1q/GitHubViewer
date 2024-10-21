package com.example.githubviewer.repository.repositorydetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.NetworkError
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
        getRepository()
    }

    private fun getRepository() {
        _screenState.value = DetailInfoScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val networkRequestResult = repository.getRepository(repositoryName)) {
                is NetworkRequestResult.Error<*> -> {
                    when (networkRequestResult.error) {
                        NetworkError.NoConnection -> {
                            _screenState.value = DetailInfoScreenState.ErrorNoConnection
                        }

                        is NetworkError.OtherError -> {
                            _screenState.value = DetailInfoScreenState.ErrorOther(
                                error = networkRequestResult.error.message
                            )
                        }
                    }
                }

                is NetworkRequestResult.Success -> {
                    _screenState.value = DetailInfoScreenState.Loaded(
                        repoDetails = networkRequestResult.data
                    )
                }
            }
        }
    }

    companion object {
        const val REPOSITORY_NAME = "REPOSITORY_NAME"
    }
}