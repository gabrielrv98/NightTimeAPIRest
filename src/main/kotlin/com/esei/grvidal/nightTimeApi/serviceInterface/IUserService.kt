package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
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

    //Save a new user
    fun save(user: UserDTOInsert): Long

    @Throws(NotFoundException::class)
    fun update(idUser: Long, user: UserDTOEdit)

    //remove an user
    @Throws(NotFoundException::class)
    fun remove(idUser: Long)

    @Throws(NotFoundException::class)
    fun login(nickname: String, password: String): Boolean


}