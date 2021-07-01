package com.esei.grvidal.nightTimeApi.projections

import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate

interface EventProjection {
    fun getId(): Long
    fun getDescription() : String
    fun getDate() : LocalDate?
    @Value("#{target.bar.name}")
    fun getBarName(): String
}