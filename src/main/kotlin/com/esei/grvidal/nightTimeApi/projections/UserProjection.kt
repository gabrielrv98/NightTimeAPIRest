package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate

interface UserProjection{

    fun getId() : Long
    fun getName() : String
    fun getNickname() : String
    fun getState() : String
    fun getEmail(): String
    fun getBirthdate() : LocalDate
    fun getDateCity() : DateCityProjection?


}

