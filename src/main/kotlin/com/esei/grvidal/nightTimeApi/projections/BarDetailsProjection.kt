package com.esei.grvidal.nightTimeApi.projections

import org.springframework.beans.factory.annotation.Value


interface BarDetailsProjection{

    fun getId() : Long
    fun getEvents() : List<EventFromBarProjection>?
    @Value("#{target.photos.size()}")
    fun getPhotos()  : Int

}



