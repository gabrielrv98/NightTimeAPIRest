package com.esei.grvidal.nightTimeApi.dto

import java.time.LocalDate

data class DateCityDTO(
        val nextDate: LocalDate,
        val nextCityId: Long
)