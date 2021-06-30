package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.DateCityReducedProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.projections.UserSnapView
import kotlin.jvm.Throws

/**
 * Service Interface for Users
 */
interface IUserService {

    //List all the user
    fun list(): List<UserProjection>

    //Show one user ( public attributes )
    @Throws(NotFoundException::class)
    fun loadProjection(idUser: Long): UserProjection

    //Show one user ( private attributes )
    @Throws(NotFoundException::class)
    fun loadEditProjection(idUser: Long): UserDTOEdit

    fun loadUserDatesList(idUser: Long, dateCityDTO: DateCityDTO): List<DateCityReducedProjection>

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

    @Throws(NotFoundException::class)
    fun deleteDate(idUser: Long, dateCity: DateCityDTO) : Long

    fun exists(idUser: Long): Boolean

    //Get the total of users on a date in a specific city
    fun getTotal(dateCityDTO: DateCityDTO): Int

    @Throws(NotFoundException::class)
    fun setUserPicture(id: Long, src: String?)

    @Throws(NotFoundException::class)
    fun getPicture(id: Long): String?

    fun searchUsersByString(userString: String, page: Int, size: Int): List<UserSnapView>
    fun countUsersByString(userString: String): Int

}