package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate
import java.time.LocalTime

interface MessageProjection{

    fun getId() : Long
    fun getText() : String
    fun getDate() : LocalDate
    fun getHour() : LocalTime
    fun getUser() : UserIdProjection


}

interface UserIdProjection{
    fun getId(): Long
}
