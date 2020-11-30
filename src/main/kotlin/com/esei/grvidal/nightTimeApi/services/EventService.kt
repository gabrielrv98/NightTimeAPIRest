package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.EventRepository
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
    val eventRepository: EventRepository? = null


    /**
     * This will list all the events, if not, will throw a BusinessException
     */
    @Throws(ServiceException::class)
    override fun list(): List<EventProjection> {
        return eventRepository?.findAllBy() ?: listOf()
    }


    @Throws(ServiceException::class, NotFoundException::class)
    override fun listEventByBar(idBar: Long): List<EventProjection> {

        return eventRepository?.findAllByBar_Id(idBar) ?: listOf()

    }


    /**
     * This will show one bar, if not, will throw a BusinessException or if the object cant be found, it will throw a NotFoundException
     */
    @Throws(ServiceException::class)
    override fun load(idEvent: Long): Event? {
        val op: Optional<Event>
        eventRepository?.let{

            op = it.findById(idEvent)
            return if (op.isPresent) {
                op.get()
            }else null
        }
        throw ServiceException("Repository error")

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(ServiceException::class)
    override fun save(event: Event): Event {

        return eventRepository?.save(event) ?: throw ServiceException("Element faild to save")
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(idEvent: Long) {
        val op: Optional<Event>

        try {
            op = eventRepository!!.findById(idEvent)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al evento con el id $idEvent")
        } else {

            try {
                eventRepository!!.deleteById(idEvent)
            } catch (e: Exception) {
                throw ServiceException(e.message)
            }
        }

    }


}


















