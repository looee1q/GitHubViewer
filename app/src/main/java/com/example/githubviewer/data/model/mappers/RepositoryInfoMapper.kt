package com.example.githubviewer.data.model.mappers

import com.example.githubviewer.data.model.RepositoryInfoDto
import com.example.githubviewer.domain.model.RepositoryInfo

class RepositoryInfoMapper : Mapper<RepositoryInfoDto, RepositoryInfo> {
    override fun map(input: RepositoryInfoDto): RepositoryInfo {
        return RepositoryInfo(
            id = input.id,
            name = input.name,
            description = input.description.orEmpty(),
            language = input.language.orEmpty(),
        )
    }
}
