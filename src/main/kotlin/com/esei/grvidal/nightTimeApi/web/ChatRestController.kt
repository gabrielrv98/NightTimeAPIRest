package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.business.IFriendsBusiness
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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
    val friendsBusiness: IFriendsBusiness? = null


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Chats // TESTING PROPOSE
     */
    @GetMapping("")
    fun list(): ResponseEntity<List<Friends>> {
        return try {
            ResponseEntity(friendsBusiness!!.list(), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Chat
     * //todo return all the messages in this chat
     */
    @GetMapping("/{id}")
    fun load(@PathVariable("id") idChat: Long): ResponseEntity<Any> {
        return try {
            ResponseEntity(friendsBusiness!!.load(idChat), HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Chat to create a chat entry
     * @param friends new Chat to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody friends: Friends): ResponseEntity<Any> {
        return try {
            friendsBusiness!!.save(friends)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_CHAT + "/" + friends.id)
            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Chat // TESTING PROPOSE
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idChat: Long): ResponseEntity<Any> {
        return try {
            friendsBusiness!!.remove(idChat)
            ResponseEntity(HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}