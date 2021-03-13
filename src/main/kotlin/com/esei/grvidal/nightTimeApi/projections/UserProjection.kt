package com.esei.grvidal.nightTimeApi.projections

import org.springframework.beans.factory.annotation.Value


interface UserProjection{

    fun getId() : Long
    fun getName() : String
    fun getNickname() : String
    fun getState() : String
    @Value("#{(target.nextDates.size > 0 ? target.nextDates[0] : null) }")
    fun getNextDate() :  DateCityProjection?
    fun getPicture() : String?

}

