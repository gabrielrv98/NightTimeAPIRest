package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.EventDTOEdit
import com.esei.grvidal.nightTimeApi.dto.EventDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toEvent
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import com.esei.grvidal.nightTimeApi.utils.Constants.Companion.URL_BASE_EVENT
import com.esei.grvidal.nightTimeApi.utils.Constants.Companion.ERROR_HEADER_TAG
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is the Event Controller
 */
@RestController
@RequestMapping(URL_BASE_EVENT)
class EventRestController {

    @Autowired
    private lateinit var eventService: IEventService

    @Autowired
    private lateinit var barService: IBarService

    /**
     * Listen to a Post with the [URL_BASE_EVENT] and a requestBody with a Event to create a Event
     */
    @PostMapping("")
    fun insert(
        @RequestBody eventDTO: EventDTOInsert
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        return try{//check if bar exists, if it does, save via DTO
            val eventId = eventService.save(
                    eventDTO.toEvent(barService.load(eventDTO.barId))
            )
            responseHeader.set("location", "$URL_BASE_EVENT/$eventId")
            ResponseEntity(responseHeader, HttpStatus.CREATED)

        }catch(e: NotFoundException){
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false,responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Put with the [URL_BASE_EVENT] and a requestBody with a Event to update a Event
     *
     */
    @PatchMapping("/{id}")
    fun update(
        @PathVariable("id") eventId: Long,
        @RequestBody event: EventDTOEdit
    ):
            ResponseEntity<Boolean> {

        return try {
            eventService.update(eventId, event)
            ResponseEntity(true,HttpStatus.OK)

        }catch(e: NotFoundException){
            val responseHeader = HttpHeaders()
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false,responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Delete with the [URL_BASE_EVENT] and a Id as a parameter to delete a Event
     */
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") idEvent: Long
    ): ResponseEntity<Boolean> {
        return try {
            eventService.remove(idEvent)
            ResponseEntity(true,HttpStatus.NO_CONTENT)

        } catch (e: NotFoundException) {
            val responseHeader = HttpHeaders()
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false,responseHeader, HttpStatus.NOT_FOUND)
        }
    }
}