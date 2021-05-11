package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.dto.MessageForm
import com.esei.grvidal.nightTimeApi.exception.NoAuthorizationException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.NotLoggedException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.MessageView
import com.esei.grvidal.nightTimeApi.serviceInterface.IMessageService
import com.esei.grvidal.nightTimeApi.utils.Constants
import com.esei.grvidal.nightTimeApi.utils.Constants.Companion.URL_BASE_MESSAGE
import com.esei.grvidal.nightTimeApi.utils.TokenSimple.TokenSimple.securityCheck
import com.pusher.rest.Pusher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(URL_BASE_MESSAGE)
class MessageRestController {


    @Autowired
    lateinit var messageService: IMessageService


    val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    //private val pusher = Pusher("PUSHER_APP_ID", "PUSHER_APP_KEY", "PUSHER_APP_SECRET")
    private val pusher = Pusher("1199159", "55882193049f9860a4af", "e5a502dd57c4eb3bbaaa")

    init {
        pusher.setCluster("eu")
        pusher.setEncrypted(true)
    }

    @PostMapping
    fun postMessage(
        @RequestHeader("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody message: MessageForm,
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(Constants.ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)

            } else {
                logger.info("message recived before save $message , sending from $idUser")
                val messageDB = messageService.save(message, idUser)

                logger.info("message received $message , sending from $idUser")

                // Send notification to the user
                pusher.trigger(
                    message.friendshipId.toString(), // Channel
                    "new_message", // Event name
                    messageDB  // Object
                )

                ResponseEntity(true, responseHeader, HttpStatus.OK)
            }
        } catch (e: NotLoggedException) {
            responseHeader.set(Constants.ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)

        } catch (e: NotFoundException) {
            responseHeader.set(Constants.ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NoAuthorizationException) {
            responseHeader.set(Constants.ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)

        }catch (e: ServiceException) {
            responseHeader.set(Constants.ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}