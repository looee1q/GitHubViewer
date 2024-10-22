package com.example.githubviewer.data.model.mappers

import com.example.githubviewer.data.model.RepoReadmeDto
import com.example.githubviewer.domain.model.RepoReadme
import javax.inject.Inject

class RepoReadmeMapper @Inject constructor() : Mapper<RepoReadmeDto, RepoReadme> {
    override fun map(input: RepoReadmeDto): RepoReadme {
        return RepoReadme(
            content = input.content.orEmpty()
        )
    }
}
