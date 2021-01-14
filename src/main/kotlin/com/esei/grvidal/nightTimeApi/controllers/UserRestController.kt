package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendRequestInsertDTO
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NoAuthorizationException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.*
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * This is the User Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_USER)
class UserRestController {

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var friendsBusiness: IFriendsBusiness

    @Autowired
    lateinit var dateCityService: IDateCityService

    @Autowired
    lateinit var cityService: ICityService

    @Autowired
    lateinit var friendRequestService: IFriendRequestService


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] to show all Bars
     */
    @GetMapping("")
    fun list(): ResponseEntity<List<UserProjection>> {
        return ResponseEntity(userService.list(), HttpStatus.OK)

    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     */
    @GetMapping("/{id}")
    fun loadProjection(@PathVariable("id") idUser: Long): ResponseEntity<Any> {

        return try {
            ResponseEntity(userService.loadProjection(idUser), HttpStatus.OK)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }



    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * @param user new Bar to insert in the database
     */
    @PostMapping("")
    fun insert(@RequestBody user: UserDTOInsert): ResponseEntity<Any> {


        val id = userService.save(user)
        val responseHeader = HttpHeaders()
        responseHeader.set("location", Constants.URL_BASE_USER + "/" + id)

        return ResponseEntity(responseHeader, HttpStatus.CREATED)

    }

    /**
     * TODO finish this
     *
     */
    @PostMapping("/login")
    fun login(
        @RequestHeader(name = "username") username: String,//todo esto o UserLoginDTO
        @RequestHeader(name = "password") password: String,
    ): ResponseEntity<Any> {
        //todo send Token
        return try {
            val isUser = userService.login(username, password)

            if (isUser) {
                // SecurityContextHolder.getContext().authentication = Authentication()
                // val authentication: Authentication = SecurityContextHolder.getContext().authentication;
                // val currentPrincipalName: String = authentication.getName();

                ResponseEntity(true, HttpStatus.OK)
            } else {
                val responseHeader = HttpHeaders()
                responseHeader.set("Error", "Credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.BAD_REQUEST)
            }


        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }


    /**
     * Listen to a Patch with the [Constants.URL_BASE_BAR] and a requestBody with a [UserDTOEdit] to update a User
     *
     * @param idUser id of the user that will be updated
     * @param user attributes to modify
     *
     * No nickname changes
     */
    @PatchMapping("/{id}")
    fun update(@PathVariable("id") idUser: Long, @RequestBody user: UserDTOEdit): ResponseEntity<Any> {

        return try {

            userService.update(idUser, user)
            ResponseEntity(HttpStatus.OK)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * Listen to a Delete with the [Constants.URL_BASE_BAR] and a Id as a parameter to delete a Bar
     *
     * @param idUser id of the bar that will be updated
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") idUser: Long): ResponseEntity<Any> {
        return try {
            userService.remove(idUser)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }


    /**
     * DateCity Related methods
     */


    /**
     * Listen to a Put with a [DateCityDTO] and an User ID, then adds the DateCity to the db
     *
     * @param idUser id of the user that will be updated
     * @param dateCity specification of the new date
     *
     * Checks that the idUser and the nextCityId exists, if they don't it returns an HTTPResponse with the code [HttpStatus.NOT_FOUND]
     * otherwise will try to update it. In case the date already existed it will respond with a [HttpStatus.ALREADY_REPORTED]
     */
    @PutMapping("/{id}/date")
    fun addDate(@PathVariable("id") idUser: Long, @RequestBody dateCity: DateCityDTO): ResponseEntity<Any> {

        return if (!userService.exists(idUser))
            ResponseEntity("No user with id $idUser found", HttpStatus.NOT_FOUND)
        else if (!cityService.exists(dateCity.nextCityId))
            ResponseEntity("No city with id ${dateCity.nextCityId} found", HttpStatus.NOT_FOUND)
        else {
            try {
                dateCityService.addDate(idUser, dateCity)
                ResponseEntity(HttpStatus.OK)
            } catch (e: AlreadyExistsException) {
                ResponseEntity(e.message, HttpStatus.ALREADY_REPORTED)
            }
        }


    }

    /**
     * Listen to a DELETE with a [idDate] and an User ID, then deletes the DateCity from the db
     *
     * @param idUser id of the user that will be updated
     * @param idDate Id of the date
     *
     * Try to delete it, if there is no problem it will return [HttpStatus.NO_CONTENT] or if the [idDate] doesn't exist
     * it will return [HttpStatus.NOT_FOUND] or if the [idUser] doesn't match with the owner of the [idDate]
     * it will return [HttpStatus.FORBIDDEN]
     */
    @DeleteMapping("/{id}/date/{idDate}")
    fun deleteDate(@PathVariable("id") idUser: Long, @PathVariable("idDate") idDate: Long): ResponseEntity<Any> {
        return try {

            userService.deleteDate(idUser, idDate)
            ResponseEntity("Successfully delete",HttpStatus.NO_CONTENT)

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NoAuthorizationException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     *  Friends related code starts here
     */

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return A List<[UserProjection]> with all the friends of the user
     *
     */
    @GetMapping("/{id}/friends/users")
    fun getUsersFromFriendList(@PathVariable("id") idUser: Long): ResponseEntity<List<UserProjection>> {

        return ResponseEntity(
            friendsBusiness.listUsersFromFriendsByUser(idUser),
            HttpStatus.OK)

    }

    /**
     * Listen to a Post with a requestBody with a [FriendRequest] to request a new [Friends]
     *
     * @param friendRequest new [FriendRequest] to insert in the database
     *
     *
     */
    @PostMapping("/{id}/friends")
    fun insertRequestFriendShip(
        @PathVariable("id") idUser: Long,
        @RequestBody friendRequest: FriendRequestInsertDTO
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        return try {

            //Checks the user who made the post is the one who asks
            if (idUser == friendRequest.idUserAsk) {

                val id = friendRequestService.save(friendRequest)

                responseHeader.set("location", "${Constants.URL_BASE_USER}/$idUser/chat/$id")
                ResponseEntity(responseHeader, HttpStatus.CREATED)

            } else {
                responseHeader.set("Error", "User must be the one who asks")
                ResponseEntity(responseHeader, HttpStatus.FORBIDDEN)
            }

        }catch (e: NotFoundException) {
            responseHeader.set("User not found", e.message)
            ResponseEntity(responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: AlreadyExistsException) {
            responseHeader.set("Error", e.message)
            ResponseEntity(responseHeader, HttpStatus.ALREADY_REPORTED)
        }
    }

    @PatchMapping("/{id}/friends")
    fun updateRequest(
        @PathVariable("id") idUser: Long,
        @RequestBody friendRequest: FriendRequest
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        val friendRequestDB = friendRequestService.load(friendRequestId = friendRequest.id)
        try {

            //check the idUser is the user who can update the Request
            if (idUser == friendRequestDB.userAnswer.id) {

                //Answer yes
                when (friendRequest.answer) {
                    AnswerOptions.YES -> {

                        //create new friendship
                        val newFriends = Friends(friendRequestDB.userAsk, friendRequestDB.userAnswer)
                        friendsBusiness.save(newFriends)
                        responseHeader.set("location", Constants.URL_BASE_USER + "/" + newFriends.id)

                        friendRequestService.remove(friendRequestDB.id)
                        return ResponseEntity(responseHeader, HttpStatus.OK)

                        //answer no
                    }
                    AnswerOptions.NO -> {

                        //remove request
                        friendRequestService.remove(friendRequestDB.id)
                        return ResponseEntity(responseHeader, HttpStatus.OK)

                    }
                    else -> {
                        return ResponseEntity(HttpStatus.ALREADY_REPORTED)
                    }
                }

            }
            //User has no permission
            responseHeader.set("Error", "Only userAnswer can update the request")
            return ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)


        } catch (e: AlreadyExistsException) {
            responseHeader.set("Error", e.message)
            return ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: ServiceException) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * //todo not sure how to handle this
     * @return The check of a deleted [Friends]
     */
    @DeleteMapping("/{id}/friends/{idFriends}")
    fun deleteFriendship(
        @PathVariable("id") idUser: Long,
        @PathVariable("idFriends") idFriends: Long
    ): ResponseEntity<Any> {
        return try {
            val responseHeader = HttpHeaders()

            val friends = friendsBusiness.load(idFriends)

            if (friends.user1.id == idUser || friends.user2.id == idUser) {
                friendsBusiness.remove(idFriends)

                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                responseHeader.set("Error", "User is not the friendship")
                ResponseEntity(HttpStatus.NOT_FOUND)
            }

        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return a [List<[Friends]>] with all the friends with any messages
     */
    @GetMapping("/{idUser}/chat")
    fun getChatWithMessages(@PathVariable("idUser") idUser: Long): ResponseEntity<Any> {
        return try {

            var filtered: List<Friends> = listOf()
            //move this to services
/*
            friendsBusiness.listChatsByUser(idUser).onEach {
                if (it.messages != null) {
                    if (it.messages!!.isNotEmpty())
                        filtered = filtered.plus(it)
                }
            }

 */

            ResponseEntity(filtered, HttpStatus.OK)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return a [Friends] object with the specified [idFriend] if the [idUser] is in that [Friends]
     */
    @GetMapping("/{idUser}/chat/{idFriend}")
    fun loadFriendWithMessages(
        @PathVariable("idUser") idUser: Long,
        @PathVariable("idFriend") idFriend: Long,
    ): ResponseEntity<Any> {

        return try {
            //Security checks. If the idChat belongs to any Chat of the user
            // In the future idUser will be a secure hashed string
            val friends = friendsBusiness.load(idFriend)
            if (friends.user1.id != idUser && friends.user2.id != idUser)
                ResponseEntity(HttpStatus.NOT_FOUND)
            else
                ResponseEntity(friends, HttpStatus.OK)


        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


    /**
     * Listen to a Post with the [Constants.URL_BASE_USER] and a requestBody with a Message to create a Message
     * Save a message with the check that
     *      the [idUser] is in the [Friends]
     *      the user who signed the message is on the relation
     *
     * @param msg new [Message] to insert in the database
     */
    @PostMapping("/{idUser}/chat")
    fun insertMessage(@PathVariable("idUser") idUser: Long, @RequestBody msg: Message): ResponseEntity<Any> {
        return try {

            val responseHeader = HttpHeaders()
            val friendsDB = friendsBusiness.load(msg.friends.id)
            if (friendsDB.user1.id != idUser && friendsDB.user2.id != idUser)
                ResponseEntity(HttpStatus.NOT_FOUND)
            else {
                friendsBusiness.saveMsg(msg)
                responseHeader.set("location", Constants.URL_BASE_BAR + "/" + msg.id)

                ResponseEntity(responseHeader, HttpStatus.OK)
            }
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return a [List<[Friends]>] with all the friends with any messages
     */
    /*
    @GetMapping("/{idUser}/date")
    fun getPeopleAndFriends(
            @PathVariable("idUser") idUser: Long,
            @RequestBody dateCity: DateCity,
    ): ResponseEntity<Any> {
        return try {
            val responseHeader = HttpHeaders()

            val user = userService.load(idUser)

            var number = dateCityService.getTotalPeopleByDateAndCity(dateCity.nextCity.id, dateCity.nextDate)
            if (user.dateCity?.nextCity?.id == dateCity.nextCity.id && user.dateCity?.nextDate == dateCity.nextDate) {
                number -= 1
            }

            responseHeader.set("Total", number.toString())
            var number2 = 0
            getUsersFromFriendList(idUser).body?.let {
                it.forEach { userFriend ->
                    userFriend.getDateCity()?.let { userFriendDateCity ->
                        if (userFriendDateCity.getNextDate() == dateCity.nextDate &&
                                userFriendDateCity.getNextCity().getId() == dateCity.nextCity.id)
                            number2 += 1
                    }
                }
            }
            responseHeader.set("Friends", number2.toString())

            ResponseEntity(responseHeader, HttpStatus.I_AM_A_TEAPOT)
        } catch (e: ServiceException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

     */

//todo 401 no permisos
}