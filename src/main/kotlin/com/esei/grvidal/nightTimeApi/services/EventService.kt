package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.EventRepository
import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
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
    lateinit var eventRepository: EventRepository


    /**
     * This will list all the events, if not, will throw a BusinessException
     */
    override fun list(): List<EventProjection> {
        return eventRepository.findAllBy()
    }



    override fun listEventByBar(idBar: Long): List<EventProjection> {
        return eventRepository.findAllByBar_Id(idBar)
    }

    override fun listEventByDay(date: LocalDate): List<EventProjection> {
        return eventRepository.findAllByDate(date)
    }


    /**
     * This will save a new bar, if not, will throw an Exception
     */
    override fun save(event: Event): Long {
        return eventRepository.save(event).id
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

        save(event)

    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun remove(idEvent: Long) {
        val op: Optional<Event>

        try {
            op = eventRepository.findById(idEvent)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al evento con el id $idEvent")
        } else {

            try {
                eventRepository.deleteById(idEvent)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }


}


















