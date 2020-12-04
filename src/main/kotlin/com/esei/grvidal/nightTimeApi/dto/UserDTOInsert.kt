package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.User
import java.time.LocalDate

data class UserDTOInsert(
        val name: String,
        val nickname: String,
        val password: String,
        val state: String? = null,
        val email: String,
        var birthdate: LocalDate,
)

fun UserDTOInsert.toUser(): User {
    return User(
            name,
            nickname,
            password,
            state,
            email,
            birthdate
    )
}