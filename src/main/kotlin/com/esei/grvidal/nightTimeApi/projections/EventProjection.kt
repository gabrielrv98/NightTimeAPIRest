package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate

interface EventProjection {
    fun getId(): Long
    fun getDescription() : String
    fun getDate() : LocalDate?
    fun getBarName(): String
}