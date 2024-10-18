package com.example.githubviewer.repository.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.domain.AppRepository
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

//    val token: MutableLiveData<String>
//    val state: LiveData<State>
//    val actions: Flow<Action>

    private val token = MutableStateFlow("")
    private val _screenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    val screenState: StateFlow<AuthScreenState> = _screenState.asStateFlow()

    init {
        _screenState.value = AuthScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val userAuthStatus = repository.getUserAuthStatus()) {
                is UserAuthStatus.Authorized -> {
                    Log.d(
                        "AuthViewModel",
                        "Авторизация прошла успешно. Пользователь: ${userAuthStatus.userInfo}"
                    )
                    _screenState.value = AuthScreenState.Idle
                }

                is UserAuthStatus.NotAuthorized -> {
                    Log.d(
                        "AuthViewModel",
                        "Авторизация не удалась по причине: ${userAuthStatus.message}"
                    )
                    _screenState.value = AuthScreenState.Initial
                }
            }
        }
    }

    fun onSignButtonPressed(inputToken: String) {
        if (inputToken.isNotBlank()) {
            _screenState.value = AuthScreenState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                when (val userAuthStatus = repository.singIn(inputToken)) {
                    is UserAuthStatus.Authorized -> {
                        Log.d(
                            "AuthViewModel",
                            "Авторизация прошла успешно. Пользователь: ${userAuthStatus.userInfo}"
                        )
                        _screenState.value = AuthScreenState.Idle
                    }

                    is UserAuthStatus.NotAuthorized -> {
                        Log.d(
                            "AuthViewModel",
                            "Авторизация не удалась по причине: ${userAuthStatus.message}"
                        )
                        _screenState.value = AuthScreenState.InvalidInput(userAuthStatus.message)
                    }
                }
            }
        }
    }
}