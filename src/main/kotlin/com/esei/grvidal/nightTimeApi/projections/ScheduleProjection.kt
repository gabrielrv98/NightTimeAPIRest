package com.esei.grvidal.nightTimeApi.projections

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