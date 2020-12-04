package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import java.util.*
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IUserService {

    //List all the user
    fun list(): List<UserProjection>

    //Show one user
    @Throws(NotFoundException::class)
    fun loadProjection(idUser: Long): UserProjection

    //Show one user
    @Throws(NotFoundException::class)
    fun load(idUser: Long): User

    //Save a new user
    fun save(user: User): User

    //remove an user
    @Throws(NotFoundException::class)
    fun remove(idUser: Long)

    @Throws(NotFoundException::class)
    fun login(nickname: String, password: String): Boolean

}