package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit

/**
 * User private projection sent so user can edit its attributes
 */
interface UserEditProjection {

    fun getId(): Long
    fun getNickname(): String
    fun getName(): String
    fun getState(): String?
    fun getEmail(): String
    fun getPassword(): String

}

fun UserEditProjection.toUserDTOEdit(): UserDTOEdit {
    return UserDTOEdit(
        id = getId(),
        name = getName(),
        password = getPassword(),
        email = getEmail(),
        state = getState()
    )
}

