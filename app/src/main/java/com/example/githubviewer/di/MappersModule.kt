package com.example.githubviewer.di

import com.example.githubviewer.data.model.RepositoryInfoDto
import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.data.model.mappers.RepositoryInfoMapper
import com.example.githubviewer.data.model.mappers.UserInfoMapper
import com.example.githubviewer.domain.model.RepositoryInfo
import com.example.githubviewer.domain.model.UserInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MyMappersModule {

    @Binds
    abstract fun bindRepositoryInfoMapper(
        impl: RepositoryInfoMapper
    ): Mapper<RepositoryInfoDto, RepositoryInfo>

    @Binds
    abstract fun bindUserInfoMapper(impl: UserInfoMapper): Mapper<UserInfoDto, UserInfo>
}

