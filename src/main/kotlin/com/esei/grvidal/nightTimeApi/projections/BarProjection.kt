package com.esei.grvidal.nightTimeApi.projections

import org.springframework.beans.factory.annotation.Value

interface BarProjection{

    fun getId() : Long
    fun getName() : String
    fun getOwner() : String
    fun getAddress() : String

    fun getMondaySchedule() : String?
    fun getTuesdaySchedule() : String?
    fun getWednesdaySchedule() : String?
    fun getThursdaySchedule() : String?
    fun getFridaySchedule() : String?
    fun getSaturdaySchedule() : String?
    fun getSundaySchedule() : String?

    fun getCity(): CityProjection
    //@Value("#{(target.getEvents().get(0))}")
    //fun getEvents() : Event?

}

