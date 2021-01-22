package com.esei.grvidal.nightTimeApi.projections

interface BarFullProjection {
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
    fun getEvents() : List<EventProjection>?
}
