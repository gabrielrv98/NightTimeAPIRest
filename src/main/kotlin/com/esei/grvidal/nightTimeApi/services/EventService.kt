package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.repository.EventRepository
import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.dto.EventDTOInsert
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.repository.BarRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the Business interface
 *
 */
@Service
class EventService : IEventService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var barRepository: BarRepository

    override fun listEventByDayAndCity(date: LocalDate, idCity: Long): List<EventProjection> {
        return eventRepository.findAllByDateAndBar_City_Id(date,idCity)
    }


    /**
     * This will save a new bar, if not, will throw an Exception
     */
    override fun save(event: EventDTOInsert): Long {
        val bar = barRepository.findById(event.barId)
            .orElseThrow { NotFoundException("Bar with id ${event.barId} couldn't be found")  }
        return eventRepository.save(
            Event(
                description = event.description,
                date = event.date,
                bar = bar
            )
        ).id
    }

    @Throws(NotFoundException::class)
    fun load(eventId: Long): Event{
        return eventRepository.findById(eventId).orElseThrow { NotFoundException("Any events with id $eventId have been found")  }
    }

    @Throws(NotFoundException::class)
    override fun update(eventId: Long, eventDTO: EventDTOEdit) {
        val event = load(eventId)

        event.apply {
            description = eventDTO.description ?: description
            date = eventDTO.date ?: date
        }

        eventRepository.save(event)

    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun remove(idEvent: Long) {

        val event = load(idEvent)
        eventRepository.delete(event)

    }


}


















