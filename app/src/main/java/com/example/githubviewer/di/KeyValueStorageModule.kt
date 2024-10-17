package com.example.githubviewer.di

import android.content.Context
import android.content.SharedPreferences
import com.example.githubviewer.data.KeyValueStorage
import com.example.githubviewer.data.KeyValueStorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KeyValueStorageModule {

    @Singleton
    @Binds
    abstract fun bindKeyValueStorage(impl: KeyValueStorageImpl): KeyValueStorage
}

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    private const val KEY_FILE = "KEY_FILE"

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext applicationContext: Context
    ): SharedPreferences {
        return applicationContext.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE)
    }
}