package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendshipBusiness
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is the Chat Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_CHAT)
class ChatRestController {


    @Autowired
    val friendshipBusiness: IFriendshipBusiness? = null





    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Chat
     * //todo return all the messages in this chat
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idChat: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity(friendshipBusiness!!.load(idChat), HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Chat // TESTING PROPOSE
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idChat: Long): ResponseEntity<Any> {
        return try {
            friendshipBusiness!!.remove(idChat)
            ResponseEntity(HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}