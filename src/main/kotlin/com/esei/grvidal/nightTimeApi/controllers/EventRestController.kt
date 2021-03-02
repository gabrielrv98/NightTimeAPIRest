package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.dto.EventDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toEvent
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
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

    @Autowired
    lateinit var barService: IBarService

    /**
     * Listen to a Post with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to create a Event
     */
    @PostMapping("")
    fun insert(@RequestBody eventDTO: EventDTOInsert): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        return try{//check if bar exists, if it does, save via DTO
            val eventId = eventService.save(
                    eventDTO.toEvent(barService.load(eventDTO.barId))
            )
            responseHeader.set("location", Constants.URL_BASE_EVENT + "/" + eventId)
            ResponseEntity(responseHeader, HttpStatus.CREATED)

        }catch(e: NotFoundException){
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }


    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to update a Event
     *
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") eventId: Long, @RequestBody event: EventDTOEdit): ResponseEntity<Any> {

        return try {
            eventService.update(eventId, event)
            ResponseEntity(HttpStatus.OK)

        }catch(e: NotFoundException){
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_EVENT] and a Id as a paramter to delete a Event
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idEvent: Long): ResponseEntity<Any> {
        return try {
            eventService.remove(idEvent)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message,HttpStatus.NOT_FOUND)
        }
    }
}