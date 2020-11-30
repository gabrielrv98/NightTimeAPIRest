package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IEventService {

    //List all the events
    fun list(): List<EventProjection>

    //List all the events of an event
    fun listEventByBar(idBar: Long): List<EventProjection>

    //Show one Event
    @Throws(ServiceException::class)
    fun load(idEvent: Long): Event?

    //Save a new event
    fun save(event: Event): Event

    //remove an event
    fun remove(idEvent: Long)

}