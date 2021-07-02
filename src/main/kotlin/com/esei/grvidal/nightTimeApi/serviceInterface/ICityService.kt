package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.City
import kotlin.jvm.Throws

/**
 * Service Interface for Cities
 */
interface ICityService {

    //List all the Cities
    fun list(): List<City>

    //Show one City
    @Throws(NotFoundException::class)
    fun load(idCity: Long): City

    //Save a new City
    fun save(city: City): City

    //remove a City
    @Throws(NotFoundException::class)
    fun remove(idCity: Long)
}