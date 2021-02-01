package com.esei.grvidal.nightTimeApi.repository

import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate


/**
 * Repository, this will link our Business with the Entities by JPA repository and add the basic functionalities
 *
 */

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findAllBy(): List<EventProjection>

    fun findAllByDate(date: LocalDate): List<EventProjection>
    fun findAllByBar_Id(barId: Long): List<EventProjection>
}

