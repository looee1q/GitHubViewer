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
            _userAuthStatus.value = repository.getUserAuthStatus()
        }
    }
}
