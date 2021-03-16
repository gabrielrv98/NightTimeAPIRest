package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.DateCity
import javassist.NotFoundException
import java.time.LocalDate

/**
 * DAO Interface for Bars
 */
interface IDateCityService {

    //List all the user
    fun list(): List<DateCity>

    //Save a new user
    fun save(dateCity: DateCity): DateCity

    fun getTotalPeopleByDateAndCity(nextCity_id: Long, nextDate: LocalDate): Int

    @Throws(NotFoundException::class, ServiceException::class)
    fun addDate(idUser: Long, dateCity: DateCityDTO):Long

}