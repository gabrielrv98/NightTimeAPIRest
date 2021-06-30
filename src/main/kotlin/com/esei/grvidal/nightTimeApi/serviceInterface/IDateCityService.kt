package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
/**
 * Service Interface for DateCity
 */
interface IDateCityService {

    @Throws(NotFoundException::class, ServiceException::class)
    fun addDate(idUser: Long, dateCity: DateCityDTO): Long

}