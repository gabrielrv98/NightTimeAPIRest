package com.esei.grvidal.nightTimeApi.projections

/**
 * User View to show user all fields
 * Null fields will remain unchanged
 */
data class UserEditView(
    val id: Long,
    val name: String,
    var password: String,
    val email: String,
    val state: String? = null
)

