package com.example.githubviewer.di

import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.data.model.mappers.Mapper
import com.example.githubviewer.data.model.mappers.RepoDetailsMapper
import com.example.githubviewer.data.model.mappers.RepoMapper
import com.example.githubviewer.data.model.mappers.RepoReadmeMapper
import com.example.githubviewer.data.model.mappers.UserInfoMapper
import com.example.githubviewer.domain.model.Repo
import com.example.githubviewer.domain.model.RepoDetails
import com.example.githubviewer.domain.model.RepoReadme
import com.example.githubviewer.domain.model.UserInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MyMappersModule {

    @Binds
    abstract fun bindRepoMapper(impl: RepoMapper): Mapper<RepoDto, Repo>

    @Binds
    abstract fun bindRepoDetailsMapper(impl: RepoDetailsMapper): Mapper<RepoDetailsDto, RepoDetails>

    @Binds
    abstract fun bindRepoReadmeMapper(impl: RepoReadmeMapper): Mapper<RepoReadmeDto, RepoReadme>

    @Binds
    abstract fun bindUserInfoMapper(impl: UserInfoMapper): Mapper<UserInfoDto, UserInfo>
}

