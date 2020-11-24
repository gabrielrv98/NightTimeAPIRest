package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.business.IBarBusiness
import com.esei.grvidal.nightTimeApi.business.IEventBusiness
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Event
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is the Event Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_EVENT)
class EventRestController {

    @Autowired
    val eventBusiness: IEventBusiness? = null

    @Autowired
    val barBusiness: IBarBusiness? = null


    /**
     * Listen to a Get with the [Constants.URL_BASE_EVENT] to show all Events
     */
    @GetMapping("")
    fun list(): ResponseEntity<List<Event>> {
        return try {
            ResponseEntity(eventBusiness!!.list(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //todo eliminar
    //no tan bien manera, pero pensar en algo
    @GetMapping("/bar/{id}")
    fun listByBar(@PathVariable("id") idBar: Long) : ResponseEntity<List<Event>> {
        return try {

            val bar = barBusiness!!.load(idBar)
            ResponseEntity(eventBusiness!!.listEventByBar(bar), HttpStatus.OK)

        }catch (e: NotFoundException){
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_EVENT] and an Id as a parameter to show one Event
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idEvent: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity(eventBusiness!!.load(idEvent), HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to create a Event
     */
    @PostMapping("")
    fun insert(@RequestBody event: Event): ResponseEntity<Any> {
        return try {
            eventBusiness!!.save(event)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_EVENT + "/" + event.id)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_EVENT] and a requestBody with a Event to update a Event
     * //todo si le pasas un Event con id = null crea uno nuevo
     */
    @PutMapping("")
    fun update(@RequestBody Event: Event): ResponseEntity<Any> {
        return try {

            eventBusiness!!.save(Event)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_EVENT] and a Id as a paramter to delete a Event
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idEvent: Long): ResponseEntity<Any> {
        return try {
            eventBusiness!!.remove(idEvent)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}