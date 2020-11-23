package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event

/**
 * DAO Interface for Bars
 */
interface IBarBusiness {

    //List all the bars
    fun list(): List<Bar>

    fun listWithEvents(idBar: Long): List<Event>

    //Show one bar
    fun load(idBar: Long): Bar

    //Save a new bar
    fun save(bar: Bar): Bar

    //remove a bar
    fun remove(idBar: Long)
}