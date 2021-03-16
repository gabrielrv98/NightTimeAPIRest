package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.User

/**
 * User DTO received to edit user's attributes
 * Null fields will remain unchanged
 */
data class UserDTOEdit(
    val id: Long,
    val name: String?,
    var password: String?,
    val state: String? = null,
    val email: String?
)

fun UserDTOEdit.toUser(user: User): User {
    return User(
        name = name ?: user.name,
        nickname = user.nickname,
        password = password ?: user.password,
        state = state ?: user.state,
        email = email ?: user.email,
        birthdate = user.birthdate
    ).apply {
        id = this@toUser.id
        nextDates = user.nextDates
        picture = user.picture
    }
}
