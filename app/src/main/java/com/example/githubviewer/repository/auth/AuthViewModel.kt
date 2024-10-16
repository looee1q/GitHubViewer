package com.example.githubviewer.repository.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.data.AppRepository
import com.example.githubviewer.data.NetworkClient
import com.example.githubviewer.data.model.mappers.RepositoryInfoMapper
import com.example.githubviewer.data.model.mappers.UserInfoMapper
import com.example.githubviewer.domain.model.UserAuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AppRepository(
        networkClient = NetworkClient(),
        userInfoMapper = UserInfoMapper(),
        repositoryInfoMapper = RepositoryInfoMapper()
    )

//    val token: MutableLiveData<String>
//    val state: LiveData<State>
//    val actions: Flow<Action>

    private val token = MutableStateFlow("")
    private val _screenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    val screenState: StateFlow<AuthScreenState> = _screenState.asStateFlow()

    fun onSignButtonPressed(inputToken: String) {
        if (inputToken.isNotBlank()) {
            _screenState.value = AuthScreenState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                when (val authStatus = repository.singIn(inputToken)) {
                    is UserAuthStatus.Authorized -> {
                        Log.d(
                            "AuthViewModel",
                            "Авторизация прошла успешно. Пользователь: ${authStatus.userInfo}"
                        )
                        _screenState.value = AuthScreenState.Idle
                    }

                    is UserAuthStatus.NotAuthorized -> {
                        Log.d(
                            "AuthViewModel",
                            "Авторизация не удалась по причине: ${authStatus.message}"
                        )
                        _screenState.value = AuthScreenState.InvalidInput(authStatus.message)
                    }
                }
            }
        }
    }
}