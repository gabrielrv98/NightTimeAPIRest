package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NoAuthorizationException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
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
    @Throws(ServiceException::class, AlreadyExistsException::class)
    fun save(user: UserDTOInsert): Long

    @Throws(NotFoundException::class)
    fun update(idUser: Long, user: UserDTOEdit)

    //remove an user
    @Throws(NotFoundException::class)
    fun remove(idUser: Long)

    @Throws(NotFoundException::class, ServiceException::class)
    fun login(nickname: String, password: String): Long

    @Throws(NotFoundException::class, NoAuthorizationException::class)
    fun deleteDate(idUser: Long, idDate: Long)

    fun exists(idUser: Long): Boolean

    //Get the total of users on a date in a specific city
    fun getTotal(dateCityDTO: DateCityDTO): Int

}