package com.example.githubviewer.data.model.mappers

import com.example.githubviewer.data.model.RepoDetailsDto
import com.example.githubviewer.domain.model.RepoDetails
import javax.inject.Inject

class RepoDetailsMapper @Inject constructor() : Mapper<RepoDetailsDto, RepoDetails> {
    override fun map(input: RepoDetailsDto): RepoDetails {
        return RepoDetails(
            id = input.id,
            name = input.name,
            url = input.url,
            licenseName = input.licenseDto?.name.orEmpty(),
            forksCount = input.forksCount,
            stargazersCount = input.stargazersCount,
            watchersCount = input.watchersCount
        )
    }
}
