package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.City

/**
 * Service Interface for Cities
 */
interface ICityService {

    //List all the Cities
    fun list(): List<City>

    //Show one City
    fun load(idCity: Long): City

    //Save a new City
    fun save(city: City): City

    //remove a City
    fun remove(idCity: Long)
}