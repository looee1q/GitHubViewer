package com.example.githubviewer.data

interface KeyValueStorage {

    fun saveKey(key: String)

    fun getKey(): String

    fun removeKey()
}