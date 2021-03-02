package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate

interface DateCityProjection {
    fun getId(): Long
    fun getNextDate(): LocalDate
    fun getNextCity(): CityProjection
}

interface DateCityReducedProjection {
    fun getNextDate(): LocalDate
}