package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.dto.CityDTO
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is the Bar Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_BAR)
class BarRestController {

    @Autowired
    val barService: IBarService? = null

    @Autowired
    val eventService: IEventService? = null


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Bars
     */
    @GetMapping("/byCity/{idCity}")
    fun listByCity(@PathVariable("idCity") cityId: Long): ResponseEntity<List<BarProjection>> {
        barService?.let{
            return ResponseEntity(it.listByCity(cityId), HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Returns a list with the events of the bar that matches the ID
     */
    @GetMapping("/{id}/details")
    fun getDetails(@PathVariable("id") idBar: Long): ResponseEntity<Any> {

        barService?.let{
            return try {
                 ResponseEntity(it.getDetails(idBar), HttpStatus.OK)

            }catch(e : NotFoundException){
                ResponseEntity(e.message, HttpStatus.NOT_FOUND)
            }
        }
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

    }


    /**
     * Returns a list with the events of the bar that matches the ID
     */
    @GetMapping("/{id}/events")
    fun listWithEvents(@PathVariable("id") idBar: Long): ResponseEntity<List<EventProjection>> {
        
        eventService?.let{
            try {
                return ResponseEntity(it.listEventByBar(idBar), HttpStatus.OK)
            }catch(e : NotFoundException){
                ResponseEntity(e.message, HttpStatus.NOT_FOUND)
            }
        }
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

    }

    @GetMapping("/{id}/projection")
    fun getProjection(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        barService?.let{

            return try{
                ResponseEntity(it.getProjection(idBar), HttpStatus.OK)
            }catch (e: NotFoundException){
                ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
            }

        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idBar: Long): ResponseEntity<Any> {

        barService?.let{
            return try{
                ResponseEntity(it.getProjection(idBar) , HttpStatus.OK)
            }catch(e: NotFoundException){
                ResponseEntity(e.message,HttpStatus.NOT_FOUND)
            }
        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * @param bar new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody bar: Bar): ResponseEntity<Any> {
        return try {
            barService!!.save(bar)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_BAR + "/" + bar.id)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to update a Bar
     *
     * @param idBar id of the bar that will be updated
     * @param fields attributes to modify
     *
     * If there is any mistake in Schedule object it will just omit the pair key value
     * //TODO editar city del bar
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idBar: Long, @RequestBody fields: Map<String, Any>): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        return try {

            val bar = barService!!.load(idBar)
            fields.forEach { (k, v) ->
                when (k) {
                    "name" -> bar.name = v.toString()
                    "owner" -> bar.owner = v.toString()
                    "address" -> bar.address = v.toString()
                    "schedule" -> {
                        try {

                            val regex = """(monday|tuesday|wednesday|thursday|friday|saturday|sunday)=(true|false)""".toRegex()
                            val matchResult = regex.findAll(v.toString())


                            matchResult.iterator().forEach {
/*
                                when (it.groupValues[1]) {
                                    "monday" -> bar.schedule?.monday = it.groupValues[2] == "true"
                                    "tuesday" -> bar.schedule?.tuesday = it.groupValues[2] == "true"
                                    "wednesday" -> bar.schedule?.wednesday = it.groupValues[2] == "true"
                                    "thursday" -> bar.schedule?.thursday = it.groupValues[2] == "true"
                                    "friday" -> bar.schedule?.friday = it.groupValues[2] == "true"
                                    "saturday" -> bar.schedule?.saturday = it.groupValues[2] == "true"
                                    "sunday" -> bar.schedule?.sunday = it.groupValues[2] == "true"

                                }

 */
                            }

                        } catch (e: Exception) {
                            responseHeader.set("ScheduleError", "no properly format " + e.message)
                        }
                    }
                }
            }
            barService!!.save(bar)
            ResponseEntity(responseHeader, HttpStatus.OK)

        } catch (e: ServiceException) {
            ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        return try {

            barService!!.remove(idBar)

            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}