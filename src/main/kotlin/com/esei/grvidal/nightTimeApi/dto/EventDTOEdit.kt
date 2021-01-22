package com.esei.grvidal.nightTimeApi.dto

import java.time.LocalDate

data class EventDTOEdit(
        val description: String?,
        val date: LocalDate?
)