package com.example.githubviewer.data.model.mappers

import com.example.githubviewer.data.model.RepoDto
import com.example.githubviewer.domain.model.Repo
import javax.inject.Inject

class RepositoryInfoMapper @Inject constructor() : Mapper<RepoDto, Repo> {
    override fun map(input: RepoDto): Repo {
        return Repo(
            id = input.id,
            name = input.name,
            description = input.description.orEmpty(),
            language = input.language.orEmpty(),
        )
    }
}
