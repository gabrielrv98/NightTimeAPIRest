package com.esei.grvidal.nightTimeApi.dto

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event
import java.time.LocalDate

data class EventDTOInsert(
        val description: String,
        val date: LocalDate,
        val barId: Long
)

fun EventDTOInsert.toEvent(bar: Bar): Event {
    return Event(description,date,bar)
}