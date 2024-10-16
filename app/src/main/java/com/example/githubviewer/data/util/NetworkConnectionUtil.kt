package com.example.githubviewer.data.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun ConnectivityManager.isDeviceConnectedToNetwork(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = getNetworkCapabilities(activeNetwork)

        return when {
            capabilities == null -> false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        return activeNetworkInfo?.isConnected ?: false
    }
}