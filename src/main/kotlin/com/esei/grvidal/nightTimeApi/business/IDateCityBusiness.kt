package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.model.DateCity
import com.esei.grvidal.nightTimeApi.model.User
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IDateCityBusiness {

    //List all the user
    fun list(): List<DateCity>

    //Save a new user
    fun save(dateCity: DateCity): DateCity

    fun getTotalPeopleByDateAndCity(nextCity_id: Long, nextDate: LocalDate): Int
}