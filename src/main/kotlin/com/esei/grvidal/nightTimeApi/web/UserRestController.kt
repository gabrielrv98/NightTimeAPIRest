package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.business.ICityBusiness
import com.esei.grvidal.nightTimeApi.business.IUserBusiness
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.User
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * This is the Bar Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_USER)
class UserRestController {

    @Autowired
    val userBusiness: IUserBusiness? = null

    @Autowired
    val cityBusiness: ICityBusiness? = null


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Bars
     */
    @GetMapping("")
    fun list(): ResponseEntity<List<User>> {
        return try {
            ResponseEntity(userBusiness!!.list(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idUser: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity(userBusiness!!.load(idUser), HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * @param user new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody user: User): ResponseEntity<Any> {
        return try {
            userBusiness!!.save(user)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_USER + "/" + user.id)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Put with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to update a Bar
     *
     * @param idUser id of the bar that will be updated
     * @param fields attributes to modify
     *
     * No nickname changes for now
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idUser: Long, @RequestBody fields: Map<String, Any>): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        return try {

            val user = userBusiness!!.load(idUser)
            fields.forEach { (k, v) ->
                when (k) {
                    "name" -> user.name = v.toString()
                    "state" -> user.state = v.toString()
                    "nextDate" -> user.nextDate = LocalDate.parse(v.toString())
                    "cityNextDate" -> {
                        try {

                            val regexCity = """(id)=([0-9]+)""".toRegex()

                            val matchResult = regexCity.findAll(v.toString())


                            matchResult.iterator().forEach {

                                when (it.groupValues[1]) {
                                    "id" -> user.cityNextDate = cityBusiness!!.load(it.groupValues[2].toLong())
                                }
                            }

                        } catch (e: Exception) {
                            responseHeader.set("City", "no properly format " + e.message)
                        }
                    }
                }
            }
            userBusiness!!.save(user)
            ResponseEntity(responseHeader, HttpStatus.OK)

        } catch (e: BusinessException) {
            ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Bar
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idUser: Long): ResponseEntity<Any> {
        return try {
            userBusiness!!.remove(idUser)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}