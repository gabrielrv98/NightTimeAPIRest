package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dao.EventRepository
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the Business interface
 *
 */
@Service
class EventBusiness : IEventBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    val eventRepository: EventRepository? = null


    /**
     * This will list all the events, if not, will throw a BusinessException
     */
    @Throws(BusinessException::class)
    override fun list(): List<Event> {

        try {
            return eventRepository!!.findAll()
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }


    @Throws(BusinessException::class, NotFoundException::class)
    override fun listEventByBar(bar: Bar): List<Event> {

        try {

            return eventRepository!!.findAllByBar(bar)
            //.apply { this.forEach { it.bar = null } }

        } catch (e: NotFoundException) {
            throw e
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

    }


    /**
     * This will show one bar, if not, will throw a BusinessException or if the object cant be found, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun load(idEvent: Long): Event {
        val op: Optional<Event>
        try {
            op = eventRepository!!.findById(idEvent)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al evento con el id $idEvent")
        }

        return op.get()

    }

    /**
     * This will save a new bar, if not, will throw an Exception
     */
    @Throws(BusinessException::class)
    override fun save(event: Event): Event {

        try {
            return eventRepository!!.save(event)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }

    /**
     * This will remove a bars through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(BusinessException::class, NotFoundException::class)
    override fun remove(idEvent: Long) {
        val op: Optional<Event>

        try {
            op = eventRepository!!.findById(idEvent)
        } catch (e: Exception) {
            throw BusinessException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al evento con el id $idEvent")
        } else {

            try {
                eventRepository!!.deleteById(idEvent)
            } catch (e: Exception) {
                throw BusinessException(e.message)
            }
        }

    }

    /**
     * This will remove all the events through the bar id where it belonged,
     *  if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     */
    @Throws(NotFoundException::class)
    override fun removeAllByBar(bar: Bar) {

        try {

            eventRepository!!.findAllByBar(bar).forEach {
                eventRepository!!.delete(it)
            }

        } catch (e: Exception) {
            throw BusinessException(e.message)
        }
    }
}


















