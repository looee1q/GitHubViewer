package com.example.githubviewer.domain.model

sealed interface NetworkError {

    data object NoConnection : NetworkError

    data class OtherError(val message: String) : NetworkError
}

sealed interface NetworkRequestResult<out D> {

    data class Success<out D>(val data: D) : NetworkRequestResult<D>

    data class Error<out E : NetworkError>(val error: E) : NetworkRequestResult<Nothing>
}
