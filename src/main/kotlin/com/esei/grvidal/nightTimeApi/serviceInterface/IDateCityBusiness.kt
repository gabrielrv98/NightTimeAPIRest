package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.DateCity
import java.time.LocalDate

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