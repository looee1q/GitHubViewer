package com.example.githubviewer.repository.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubviewer.domain.AppRepository
import com.example.githubviewer.domain.model.UserAuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _userAuthStatus = MutableStateFlow<UserAuthStatus?>(null)
    val userAuthStatus = _userAuthStatus.asStateFlow()

    init {
        getUserAuthStatus()
    }

    private fun getUserAuthStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userAuthStatus = repository.getUserAuthStatus()) {
                is UserAuthStatus.Authorized -> {
                    _userAuthStatus.value = UserAuthStatus.Authorized(userAuthStatus.userInfo)
                }

                is UserAuthStatus.NotAuthorized -> {
                    _userAuthStatus.value = UserAuthStatus.NotAuthorized(userAuthStatus.message)
                }
            }
        }
    }
}
