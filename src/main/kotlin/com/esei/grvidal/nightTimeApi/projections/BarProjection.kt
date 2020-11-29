package com.esei.grvidal.nightTimeApi.projections

import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.model.Schedule
import org.springframework.beans.factory.annotation.Value

interface BarProjection{

    fun getId() : Long
    fun getName() : String
    fun getOwner() : String
    fun getAddress() : String
    fun getCity(): City
    fun getSchedule() : Schedule?

    //@Value("#{T(BarProjection).toId(target.getEvents().get(0))}")
    //fun getEvents() : List<Event>?

}

