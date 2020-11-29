package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.model.City

/**
 * DAO Interface for Cities
 */
interface ICityBusiness {

    //List all the Cities
    fun list(): List<City>

    //Show one City
    fun load(idCity: Long): City

    //Save a new City
    fun save(City: City): City

    //remove a City
    fun remove(idCity: Long)
}