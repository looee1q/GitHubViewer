package com.example.githubviewer.data.model.mappers

import com.example.githubviewer.data.model.UserInfoDto
import com.example.githubviewer.domain.model.UserInfo

class UserInfoMapper : Mapper<UserInfoDto, UserInfo> {
    override fun map(input: UserInfoDto): UserInfo {
        return UserInfo(
            id = input.id,
            login = input.login,
            name = input.name,
            url = input.url,
        )
    }
}