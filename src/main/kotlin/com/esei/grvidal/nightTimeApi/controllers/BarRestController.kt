package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.BarDTOEdit
import com.esei.grvidal.nightTimeApi.dto.BarDTOInsert
import com.esei.grvidal.nightTimeApi.dto.toBar
import com.esei.grvidal.nightTimeApi.serviceInterface.IBarService
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.projections.BarDetailsProjection
import com.esei.grvidal.nightTimeApi.projections.BarProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.ICityService
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is Bar Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_BAR)
class BarRestController {

    //Bar Service with the logic of bar
    @Autowired
    lateinit var barService: IBarService

    //City Service with the logic of city
    @Autowired
    lateinit var cityService: ICityService


    /**
     * Listen to a Get with an id from a city and shows all Bars in that city
     *
     * @param cityId Id from the city to show
     * @return all Bars in that city with id [cityId]
     */
    @GetMapping("/byCity/{idCity}")
    fun listByCity(@PathVariable("idCity") cityId: Long,
                   @RequestParam(defaultValue = "1") page: Int): ResponseEntity<List<BarProjection>> {

        return ResponseEntity(barService.listByCity(cityId,page), HttpStatus.OK)

    }

    /**
     * Listen to a get with the id of a bar to return the information of that bar that is not on the preview
     *
     * @return [BarDetailsProjection] of the bar with the matching [idBar]
     * @param idBar id of the bar
     */
    @GetMapping("/{id}/details")
    fun getDetails(@PathVariable("id") idBar: Long): ResponseEntity<Any> {


        return try {
            ResponseEntity(barService.getDetails(idBar), HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }


    }


    /**
     * Listen to a Get with the [idBar] to show all the information of a bar
     *
     * @param idBar id of the bar
     */
    @Deprecated("This method returns all the information, only for testing propose")
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idBar: Long): ResponseEntity<Any> {

        return try {
            ResponseEntity(barService.getFullProjection(idBar), HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }

    }

    /**
     * Listen to a Post and a requestBody with a [BarDTOInsert] to create a new Bar
     *
     * @param bar new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody bar: BarDTOInsert): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        return try {
            val barId = barService.save(bar.toBar(cityService.load(bar.cityId)))
            responseHeader.set("location", Constants.URL_BASE_BAR + "/" + barId)
            ResponseEntity(responseHeader, HttpStatus.CREATED)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Patch and a requestBody with a [BarDTOEdit] to update a Bar
     * if any attribute is null, the original will be used, and if any schedule is text "null", will be set as null
     *
     * @param idBar id of the bar that will be updated
     * @param barEdit bar with the attributes to modify
     *
     * If there is any mistake in Schedule object it will just omit the pair key value
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idBar: Long, @RequestBody barEdit: BarDTOEdit): ResponseEntity<Any> {

        return try {
            barService.update(idBar, barEdit)
            ResponseEntity( HttpStatus.OK)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }


    }

    /**
     * Listen to a Delete and a Id to delete a Bar
     *
     * @param idBar id of the deleted bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        return try {

            barService.remove(idBar)
            ResponseEntity(HttpStatus.NO_CONTENT)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }
}