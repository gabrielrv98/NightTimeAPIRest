package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate

interface DateCityReducedProjection {
    fun getNextDate(): LocalDate
}