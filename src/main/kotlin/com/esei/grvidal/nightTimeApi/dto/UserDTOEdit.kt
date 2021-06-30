package com.esei.grvidal.nightTimeApi.dto

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
