package com.esei.grvidal.nightTimeApi.projections

import java.time.LocalDate

interface EventFromBarProjection {
    fun getId(): Long
    fun getDescription() : String
    fun getDate() : LocalDate
}