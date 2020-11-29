package com.esei.grvidal.nightTimeApi.projections

interface CityProjection {
    fun getId() : Long
    fun getName() : String
    fun getCountry() : String
}