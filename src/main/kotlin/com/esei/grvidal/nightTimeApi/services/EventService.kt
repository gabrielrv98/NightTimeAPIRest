package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.EventRepository
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
    @Throws(ServiceException::class)
    override fun list(): List<EventProjection> {
        return eventRepository.findAllBy()
    }


    @Throws(ServiceException::class, NotFoundException::class)
    override fun listEventByBar(idBar: Long): List<EventProjection> {

        return eventRepository.findAllByBar_Id(idBar)

    }

    override fun listEventByDay(date: LocalDate): List<EventProjection> {
        return eventRepository.findAllByDate(date)
    }


    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(event: Event): Event {

        return eventRepository.save(event)
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
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


















