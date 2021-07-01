package com.esei.grvidal.nightTimeApi.dto

import java.time.LocalDate

data class EventDTOInsert(
        val description: String,
        val date: LocalDate,
        val barId: Long
)
