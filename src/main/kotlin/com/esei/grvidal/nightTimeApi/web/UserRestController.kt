package com.esei.grvidal.nightTimeApi.web

import com.esei.grvidal.nightTimeApi.business.IFriendsBusiness
import com.esei.grvidal.nightTimeApi.business.IDateCityBusiness
import com.esei.grvidal.nightTimeApi.business.IFriendRequestBusiness
import com.esei.grvidal.nightTimeApi.business.IUserBusiness
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.BusinessException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.*
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
    val userBusiness: IUserBusiness? = null

    @Autowired
    val friendsBusiness: IFriendsBusiness? = null

    @Autowired
    val dateCityBusiness: IDateCityBusiness? = null

    @Autowired
    val friendRequestBusiness: IFriendRequestBusiness? = null


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
    fun insert(@RequestBody user: User, @RequestHeader(name = "password") password: String): ResponseEntity<Any> {
        return try {
            userBusiness!!.save(user)
            userBusiness!!.saveSecretData(SecretData(password, user))

            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_USER + "/" + user.id)

            ResponseEntity(responseHeader, HttpStatus.CREATED)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}/login")
    fun login(
            @PathVariable("id") idUser: Long,
            @RequestHeader(name = "username") username: String,
            @RequestHeader(name = "password") password: String,
    ): ResponseEntity<Any> {
        var responseHeader = HttpHeaders()
        return try {
            val user = userBusiness!!.load(idUser = idUser)

            if (user.nickname == username) {
                responseHeader.set("UUID", userBusiness!!.login(user, password).toString())

            } else throw BusinessException("")

            ResponseEntity(responseHeader, HttpStatus.OK)

        } catch (e: Exception) {
            responseHeader = HttpHeaders()
            responseHeader.set("Error", "Credentials don't match")
            ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * Listen to a Patch with the [Constants.URL_BASE_BAR] and a requestBody with a User to update a User
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
                }
            }
            userBusiness!!.save(user)
            ResponseEntity(responseHeader, HttpStatus.OK)

        } catch (e: BusinessException) {
            ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}/date")
    fun updateDate(@PathVariable("id") idUser: Long, @RequestBody dateCity: DateCity): ResponseEntity<Any> {
        return try {

            val user = userBusiness!!.load(idUser)

            if (user.dateCity != null) {
                dateCity.id = user.dateCity!!.id

            } else user.dateCity = dateCity

            dateCityBusiness!!.save(dateCity)
            userBusiness!!.save(user)

            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR)
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


    /**
     *  Friends related code starts here
     */


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     *  @return  A List<[Friends]> with all the friends of the [idUser]
     */
    @GetMapping("/{id}/friends")
    fun getFriendships(@PathVariable("id") idUser: Long): ResponseEntity<List<Friends>> {
        return try {
            ResponseEntity(friendsBusiness!!.listByUser(idUser), HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return A List<[User]> with all the friends of the user
     *
     * @deprecated
     */
    @GetMapping("/{id}/friends/users")
    fun getUsersFromFriendList(@PathVariable("id") idUser: Long): ResponseEntity<List<User>> {
        return try {
            val user = userBusiness!!.load(idUser)

            val friendsList: List<Friends> = friendsBusiness!!.listByUser(idUser)
            var userList: List<User> = listOf()


            friendsList.onEach { friendship ->
                if (friendship.user1 == user) userList = userList.plus(friendship.user2)
                else if (friendship.user2 == user) userList = userList.plus(friendship.user1)
            }

            ResponseEntity(userList, HttpStatus.OK)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * @param friends new [Friends] to insert in the database
     * // todo always send friends with id = 0
     */
    @PostMapping("/{id}/friends")
    fun insertRequest(@PathVariable("id") idUser: Long, @RequestBody friendRequest: FriendRequest): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        return try {

            if (idUser == friendRequest.userAsk.id) {
                friendRequestBusiness!!.save(friendRequest)

                responseHeader.set("location", Constants.URL_BASE_USER + "/" + friendRequest.id)
                ResponseEntity(responseHeader, HttpStatus.CREATED)
            } else {
                responseHeader.set("Error", "User is not in the friendship")
                ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
            }

        } catch (e: AlreadyExistsException) {
            responseHeader.set("Error", e.message)
            ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/{id}/friends")
    fun updateRequest(@PathVariable("id") idUser: Long, @RequestBody friendRequest: FriendRequest): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        val friendRequestDB = friendRequestBusiness!!.load(friendRequestId = friendRequest.id)
        try {

            //check the idUser is the user who can update the Request
            if (idUser == friendRequestDB.userAnswer.id) {

                //Answer yes
                if (friendRequest.answer == AnswerOptions.YES) {

                    //create new friendship
                    val newFriends = Friends(friendRequestDB.userAsk, friendRequestDB.userAnswer)
                    friendsBusiness!!.save(newFriends)
                    responseHeader.set("location", Constants.URL_BASE_USER + "/" + newFriends.id)

                    friendRequestBusiness!!.remove(friendRequestDB.id)
                    return ResponseEntity(responseHeader, HttpStatus.OK)

                    //anser no
                } else if (friendRequest.answer == AnswerOptions.NO) {

                    //remove request
                    friendRequestBusiness!!.remove(friendRequestDB.id)
                    return ResponseEntity(responseHeader, HttpStatus.OK)
                }
                //Usuario no tiene permiso
            }
            responseHeader.set("Error", "Only userAnswer can update the request")
            return ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)


        } catch (e: AlreadyExistsException) {
            responseHeader.set("Error", e.message)
            return ResponseEntity(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: BusinessException) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * //todo not sure how to handle this
     * @return The check of a deleted [Friends]
     */
    @DeleteMapping("/{id}/friends/{idFriends}")
    fun deleteFriendship(@PathVariable("id") idUser: Long, @PathVariable("idFriends") idFriends: Long): ResponseEntity<Any> {
        return try {
            val responseHeader = HttpHeaders()

            val friends = friendsBusiness!!.load(idFriends)

            if (friends.user1.id == idUser || friends.user2.id == idUser) {
                friendsBusiness!!.remove(idFriends)

                ResponseEntity(HttpStatus.OK)
            } else {
                responseHeader.set("Error", "User is not the friendship")
                ResponseEntity(HttpStatus.NOT_FOUND)
            }

        } catch (e: BusinessException) {
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

            friendsBusiness!!.listByUser(idUser).onEach {
                if (it.messages != null) {
                    if (it.messages!!.isNotEmpty())
                        filtered = filtered.plus(it)
                }
            }

            ResponseEntity(filtered, HttpStatus.OK)
        } catch (e: BusinessException) {
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
            val friends = friendsBusiness!!.load(idFriend)
            if (friends.user1.id != idUser && friends.user2.id != idUser)
                ResponseEntity(HttpStatus.NOT_FOUND)
            else
                ResponseEntity(friends, HttpStatus.OK)


        } catch (e: BusinessException) {
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
            val friendsDB = friendsBusiness!!.load(msg.friends.id)
            if (friendsDB.user1.id != idUser && friendsDB.user2.id != idUser)
                ResponseEntity(HttpStatus.NOT_FOUND)
            else {
                friendsBusiness!!.saveMsg(msg)
                responseHeader.set("location", Constants.URL_BASE_BAR + "/" + msg.id)

                ResponseEntity(responseHeader, HttpStatus.OK)
            }
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return a [List<[Friends]>] with all the friends with any messages
     */
    @GetMapping("/{idUser}/date")
    fun getPeopleAndFriends(
            @PathVariable("idUser") idUser: Long,
            @RequestBody dateCity: DateCity,
    ): ResponseEntity<Any> {
        return try {
            val responseHeader = HttpHeaders()

            val user = userBusiness!!.load(idUser)

            var number = dateCityBusiness!!.getTotalPeopleByDateAndCity(dateCity.nextCity.id, dateCity.nextDate)
            if (user.dateCity?.nextCity?.id == dateCity.nextCity.id && user.dateCity?.nextDate == dateCity.nextDate) {
                number -= 1
            }

            responseHeader.set("Total", number.toString())
            var number2 = 0
            getUsersFromFriendList(idUser).body?.let {
                it.forEach { userFriend ->
                    userFriend.dateCity?.let { userFriendDateCity ->
                        if (userFriendDateCity.nextDate == dateCity.nextDate &&
                                userFriendDateCity.nextCity.id == dateCity.nextCity.id)
                            number2 += 1
                    }
                }
            }
            responseHeader.set("Friends", number2.toString())

            ResponseEntity(responseHeader, HttpStatus.I_AM_A_TEAPOT)
        } catch (e: BusinessException) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


}