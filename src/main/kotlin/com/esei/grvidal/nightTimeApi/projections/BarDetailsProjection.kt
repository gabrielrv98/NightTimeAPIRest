package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.Event
import org.springframework.beans.factory.annotation.Value

interface BarDetailsProjection{

    fun getId() : Long
    //@Suppress("UNUSED_FUNCTION")
    fun getEvents() : List<EventFromBarProjection>?

}

