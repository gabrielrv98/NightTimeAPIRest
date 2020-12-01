package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.dto.BarDTOInsert
import com.esei.grvidal.nightTimeApi.dto.CityDTO
import com.esei.grvidal.nightTimeApi.dto.toBar
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import com.esei.grvidal.nightTimeApi.serviceInterface.IEventService
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.projections.EventProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.ICityService
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
    val cityService: ICityService? = null

    @Autowired
    val eventService: IEventService? = null


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Bars
     * TODO ADD PAGEABLE
     */
    @GetMapping("/byCity/{idCity}")
    fun listByCity(@PathVariable("idCity") cityId: Long): ResponseEntity<List<BarProjection>> {
        barService?.let {
            return ResponseEntity(it.listByCity(cityId), HttpStatus.OK)
        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Returns a list with the events of the bar that matches the ID
     */
    @GetMapping("/{id}/details")
    fun getDetails(@PathVariable("id") idBar: Long): ResponseEntity<Any> {

        barService?.let {
            return try {
                ResponseEntity(it.getDetails(idBar), HttpStatus.OK)
            } catch (e: NotFoundException) {
                ResponseEntity(e.message, HttpStatus.NOT_FOUND)
            }
        }
        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)

    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idBar: Long): ResponseEntity<Any> {

        barService?.let {
            return try {
                ResponseEntity(it.getFullProjection(idBar), HttpStatus.OK)
            } catch (e: NotFoundException) {
                ResponseEntity(e.message, HttpStatus.NOT_FOUND)
            }
        }

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * @param bar new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody bar: BarDTOInsert): ResponseEntity<Any> {
        return try {
            val barId = barService!!.save(bar.toBar(cityService!!.load(bar.cityId))).id
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_BAR + "/" + barId)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Patch with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to update a Bar
     *
     * @param idBar id of the bar that will be updated
     * @param barEdit bar with the attributes to modify
     *
     * If there is any mistake in Schedule object it will just omit the pair key value
     * //TODO editar city del bar
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idBar: Long, @RequestBody barEdit: BarDTOEdit): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        //getting the old Bar
        val barOriginal = barService!!.load(idBar)

        //updating the bar
        barOriginal.apply {
            id = idBar
            name = barEdit.name ?: this.name
            owner = barEdit.owner ?: this.owner
            address = barEdit.address ?: this.address

            city = barEdit.cityId?.let {
                try {
                    //if the city id doesn't match any cities, it wont update
                    cityService!!.load(it)
                } catch (e: NotFoundException) {
                    responseHeader.set("Warning", e.message)
                    this.city
                }
            } ?: this.city

            mondaySchedule = if (barEdit.mondaySchedule == null)
                this.mondaySchedule
            else {
                if (barEdit.mondaySchedule != "null") barEdit.mondaySchedule
                else null
            }

            tuesdaySchedule = if (barEdit.tuesdaySchedule == null)
                this.tuesdaySchedule
            else {
                if (barEdit.tuesdaySchedule != "null") barEdit.tuesdaySchedule
                else null
            }

            wednesdaySchedule = if (barEdit.wednesdaySchedule == null)
                this.wednesdaySchedule
            else {
                if (barEdit.wednesdaySchedule != "null") barEdit.wednesdaySchedule
                else null
            }

            thursdaySchedule = if (barEdit.thursdaySchedule == null)
                this.thursdaySchedule
            else {
                if (barEdit.thursdaySchedule != "null") barEdit.thursdaySchedule
                else null
            }

            fridaySchedule = if (barEdit.fridaySchedule == null)
                this.fridaySchedule
            else {
                if (barEdit.fridaySchedule != "null") barEdit.fridaySchedule
                else null
            }

            saturdaySchedule = if (barEdit.saturdaySchedule == null)
                this.saturdaySchedule
            else {
                if (barEdit.saturdaySchedule != "null") barEdit.saturdaySchedule
                else null
            }

            sundaySchedule = if (barEdit.sundaySchedule == null)
                this.sundaySchedule
            else {
                if (barEdit.sundaySchedule != "null") barEdit.sundaySchedule
                else null
            }
        }


        barService!!.save(barOriginal)
        return ResponseEntity(responseHeader, HttpStatus.OK)


    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        return try {

            barService!!.remove(idBar)
            ResponseEntity(HttpStatus.NO_CONTENT)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }
}