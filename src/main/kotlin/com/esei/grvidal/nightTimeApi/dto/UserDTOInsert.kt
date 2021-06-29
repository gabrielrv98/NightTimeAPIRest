package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.User

data class UserDTOInsert(
        val name: String,
        val nickname: String,
        var password: String,
        val state: String? = null,
        val email: String
)

fun UserDTOInsert.toUser(): User {
    return User(
            name,
            nickname,
            password,
            state,
            email
    )
}