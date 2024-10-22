package com.example.githubviewer.domain.model

sealed interface NetworkError

sealed interface BaseNetworkError : NetworkError {

    data object NoConnection : BaseNetworkError

    data class OtherError(val message: String) : BaseNetworkError
}

sealed interface ExtendedNetworkError : NetworkError {

    data object NoConnection : ExtendedNetworkError

    data class OtherError(val message: String) : ExtendedNetworkError

    data object ResourceNotFoundError : ExtendedNetworkError
}

sealed interface NetworkRequestResult<out D, out E : NetworkError> {

    data class Success<out D>(val data: D) : NetworkRequestResult<D, Nothing>

    data class Error<out E : NetworkError>(val error: E) : NetworkRequestResult<Nothing, E>
}
