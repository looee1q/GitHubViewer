package com.example.githubviewer.di

import com.example.githubviewer.data.AppRepositoryImpl
import com.example.githubviewer.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository
}