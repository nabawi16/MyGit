package com.nabawi.mygit.utils

import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.data.source.remote.responses.UserResponse

object DataMapper {

    fun mapResponseToEntity(response: UserResponse): UserEntity{
        return UserEntity(
            response.id,
            response.username,
            response.avatarUrl,
            response.htmlUrl
        )
    }

    fun mapResponsesToEntities(responses: List<UserResponse>): List<UserEntity>{
        return responses.map {
            mapResponseToEntity(it)
        }
    }
}