package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event

/**
 * DAO Interface for Bars
 */
interface IEventBusiness {

    //List all the events
    fun list(): List<Event>

    //List all the events of an event
    fun listEventByBar(bar: Bar): List<Event>

    //Show one Event
    fun load(idEvent: Long): Event

    //Save a new event
    fun save(event: Event): Event

    //remove an event
    fun remove(idEvent: Long)

    fun removeAllByBar(bar: Bar)
}