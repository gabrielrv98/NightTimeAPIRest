package com.esei.grvidal.nightTimeApi.projections

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