package com.esei.grvidal.nightTimeApi.dao

import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository



/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 *
 */

@Repository//todo investigar esto
interface EventRepository : JpaRepository<Event, Long> {
    fun findAllBy(): List<EventProjection>

    fun findAllByBar_Id(barId: Long): List<EventProjection>
}

