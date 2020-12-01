package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * This is the Event Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_EVENT)
class EventRestController {

    @Autowired
    lateinit var eventService: IEventService


    /**
     * Listen to a Get with the [Constants.URL_BASE_EVENT] and an Id as a parameter to show one Event
     */
    @GetMapping("/byDay")
    fun listByDay(@RequestBody date: LocalDate): ResponseEntity<Any> {

        return ResponseEntity(eventService.listEventByDay(date), HttpStatus.OK)
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to create a Event
     */
    @PostMapping("")
    fun insert(@RequestBody event: Event): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        eventService.save(event)
        responseHeader.set("location", Constants.URL_BASE_EVENT + "/" + event.id)
        return ResponseEntity(responseHeader, HttpStatus.CREATED)

    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to update a Event
     * //todo si le pasas un Event con id = null crea uno nuevo
     */
    @PutMapping("")
    fun update(@RequestBody Event: Event): ResponseEntity<Any> {
        return try {

            eventService.save(Event)
            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_EVENT] and a Id as a paramter to delete a Event
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idEvent: Long): ResponseEntity<Any> {
        return try {
            eventService.remove(idEvent)
            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}