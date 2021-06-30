package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * Service Interface for Events
 */
interface IEventService {

    //List all the events
    fun list(): List<EventProjection>

    //List all the events of an event
    fun listEventByBar(idBar: Long): List<EventProjection>

    //List all the events of an event
    fun listEventByDayAndCity(date: LocalDate, idCity:Long): List<EventProjection>

    //Save a new event
    fun save(event: Event): Long

    //Updates an existing Event
    @Throws(ServiceException::class)
    fun update(eventId: Long, eventDTO: EventDTOEdit)

    //remove an event
    @Throws(NotFoundException::class)
    fun remove(idEvent: Long)

}