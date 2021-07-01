package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.dto.EventDTOInsert
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * Service Interface for Events
 */
interface IEventService {

    //List all the events of an event
    fun listEventByDayAndCity(date: LocalDate, idCity:Long): List<EventProjection>

    //Save a new event
    @Throws(NotFoundException::class)
    fun save(event: EventDTOInsert): Long

    //Updates an existing Event
    @Throws(ServiceException::class)
    fun update(eventId: Long, eventDTO: EventDTOEdit)

    //remove an event
    @Throws(NotFoundException::class)
    fun remove(idEvent: Long)

}