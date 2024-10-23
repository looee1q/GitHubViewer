package com.example.githubviewer.repository.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.BaseNetworkError
import com.example.githubviewer.domain.model.UserAuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    val screenState: StateFlow<AuthScreenState> = _screenState.asStateFlow()

    fun onSignButtonPressed(inputToken: String) {
        if (inputToken.isNotBlank()) {
            _screenState.value = AuthScreenState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                when (val userAuthStatus = repository.singIn(inputToken)) {
                    is UserAuthStatus.Authorized -> _screenState.value = AuthScreenState.Idle
                    is UserAuthStatus.NotAuthorized -> {
                        when (userAuthStatus.baseNetworkError) {
                            BaseNetworkError.NoConnection -> {
                                _screenState.value = AuthScreenState.NoConnection
                            }

                            is BaseNetworkError.OtherError -> {
                                _screenState.value = AuthScreenState.InvalidInput(
                                    userAuthStatus.baseNetworkError.message
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}