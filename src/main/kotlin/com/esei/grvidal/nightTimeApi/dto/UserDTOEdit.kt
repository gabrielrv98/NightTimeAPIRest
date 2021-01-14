package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.User
import java.time.LocalDate

class UserDTOEdit(
        val name: String?,
        val password: String?,
        val state: String? = null,
        val email: String?
)

fun UserDTOEdit.toUser(user: User): User {
        return User(
                name ?: user.name,
                nickname = user.nickname,
                password ?: user.password,
                state,
                email ?: user.email,
                birthdate = user.birthdate
        ).apply { id = user.id }
}
