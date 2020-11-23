package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.business.IBarBusiness
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Bar
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
    val barBusiness: IBarBusiness? = null

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Bars
     */
    @GetMapping("")
    fun list(): ResponseEntity<List<Bar>> {
        return try {
            ResponseEntity(barBusiness!!.list(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity(barBusiness!!.load(idBar), HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     */
    @PostMapping("")
    fun insert(@RequestBody bar: Bar): ResponseEntity<Any> {
        return try {
            barBusiness!!.save(bar)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_BAR + "/" + bar.id)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to update a Bar
     */
    @PutMapping("")
    fun update(@RequestBody bar: Bar): ResponseEntity<Any> {
        return try {
            barBusiness!!.save(bar)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a paramter to delete a Bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idBar: Long): ResponseEntity<Any> {
        return try {
            barBusiness!!.remove(idBar)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}