package com.esei.grvidal.nightTimeApi.model

import javax.persistence.*

interface ScheduleProjection {
    fun getMonday(): Boolean
    fun getTuesday(): Boolean
    fun getWednesday(): Boolean
    fun getThursday(): Boolean
    fun getFriday(): Boolean
    fun getSaturday(): Boolean
    fun getSunday(): Boolean
}