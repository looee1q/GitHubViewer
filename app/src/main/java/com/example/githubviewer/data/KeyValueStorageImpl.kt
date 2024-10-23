package com.example.githubviewer.data

import android.content.SharedPreferences
import javax.inject.Inject

class KeyValueStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : KeyValueStorage {

    override fun saveKey(key: String) {
        sharedPreferences.edit().putString(STORAGE_KEY_OF_USER_KEY, key).apply()
    }

    override fun getKey(): String {
        return sharedPreferences.getString(STORAGE_KEY_OF_USER_KEY, EMPTY_KEY) ?: EMPTY_KEY
    }

    override fun removeKey() {
        return sharedPreferences.edit().remove(STORAGE_KEY_OF_USER_KEY).apply()
    }

    companion object {
        private const val STORAGE_KEY_OF_USER_KEY = "STORAGE_KEY_OF_USER_KEY"
        private const val EMPTY_KEY = ""
    }
}