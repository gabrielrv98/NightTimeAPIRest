package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import java.util.*

/**
 * DAO Interface for Bars
 */
interface IUserService {

    //List all the user
    fun list(): List<UserProjection>

    //Show one user
    fun load(idUser: Long): User

    //Save a new user
    fun save(user: User): User

    //remove an user
    fun remove(idUser: Long)

    fun login(nickname: String, password: String): Boolean

}