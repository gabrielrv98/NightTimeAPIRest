package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.model.Schedule
import org.springframework.beans.factory.annotation.Value

interface BarDetailsProjection{

    fun getId() : Long
    //@Value("#{T(BarProjection).toId(target.getEvents().get(0))}")
    fun getEvents() : List<EventProjection>?

}

