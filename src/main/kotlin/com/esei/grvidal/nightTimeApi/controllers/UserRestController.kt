package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.*
import com.esei.grvidal.nightTimeApi.exception.*
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.projections.*
import com.esei.grvidal.nightTimeApi.serviceInterface.*
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import kotlin.collections.HashMap
import kotlin.jvm.Throws

/**
 * This is the User Controller
 *
 */
@RestController
@RequestMapping(Constants.URL_BASE_USER)
class UserRestController {

    //Service injections
    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var friendshipService: IFriendshipService

    @Autowired
    lateinit var dateCityService: IDateCityService

    @Autowired
    lateinit var messageService: IMessageService

    @Autowired
    lateinit var cityService: ICityService

    @Autowired
    lateinit var eventService: IEventService

    //HashMap with the tokens for authorization
    private val tokenSimple: HashMap<Long, String> = hashMapOf()

    /**
     * Receives [id] of the user that is also the key for the hashMap, [token] as the secure string
     *
     * @return true if the parameter token equals the saved token
     * @exception [NotLoggedException] if the token for the [id] is null
     */
    @Throws(NotLoggedException::class)
    private fun securityCheck(id: Long, token: String): Boolean {
        tokenSimple[id]?.let {
            return it == token
        }
        throw NotLoggedException("user with id $id is not logged in")
    }

    /**
     * Receives [length] as an int for the length of the generated string
     *
     * @return a random string of length [length] and characters from the array [charset]
     */
    fun tokenGen(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789$@-_=//\\*$&" // 71 characters
        var token: String

        do {
            token = (1..length)
                .map { charset.random() }
                .joinToString("")

        } while (tokenSimple.values.contains(token))//If token is repeated, a new one is calculated

        return token
    }


    /**
     * Receives an [username] and a [password] and checks if the [username] exists,
     *  if it does, will check if password is valid.
     *  If everything is correct it will return the user id and a token for future request
     *
     *  @return id and a token for future request
     *
     */
    @PostMapping("/login")
    fun login(
        @RequestHeader(name = "username") username: String,
        @RequestHeader(name = "password") password: String,
    ): ResponseEntity<Any> {
        return try {
            val idUser = userService.login(username, password)
            val responseHeader = HttpHeaders()

            if (idUser != -1L) {

                val token = tokenGen(25) //max users online: 25 char * 71 charset = 1775 users

                tokenSimple[idUser] = token
                responseHeader.set("id", idUser.toString())
                responseHeader.set("token", token)
                ResponseEntity(true, responseHeader, HttpStatus.ACCEPTED)


            } else {

                responseHeader.set("Error", "Credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.BAD_REQUEST)
            }


        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: ServiceException) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    /**
     * Receives an [idUser] as the id of the user and [auth] as the authorization token,
     * If the token was correct it is deleted from the hashmap
     *
     */
    @PostMapping("/{id}/logoff")
    fun logoff(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {

        return try {

            if (securityCheck(idUser, auth)) {
                tokenSimple.remove(idUser)
                ResponseEntity("Good bye", HttpStatus.ACCEPTED)

            } else
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    @Deprecated("Testing proposes")
    @GetMapping("/alllogins")
    fun getLogged2(): ResponseEntity<Any> {

        return ResponseEntity(tokenSimple, HttpStatus.OK)
    }

    /**
     * Listen to a Get with the [Constants.URL_BASE_USER] to show all Users //todo check usefulness
     */
    @Deprecated("Testing proposes")
    @GetMapping("")
    fun list(): ResponseEntity<List<UserProjection>> {
        return ResponseEntity(userService.list(), HttpStatus.OK)

    }


    /**
     * Receives an [idUser] and returns a UserProjection //TODO IS THIS PUBLIC?
     *
     * @exception NotFoundException the [idUser] is wrong [HttpStatus.NOT_FOUND] will be shown
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
     * Returns a list with the selected dates of the user in a specific city
     */
    @GetMapping("/{id}/day-list/{idCity}")
    fun loadFutureUserDatesListFromCity(
        @RequestHeader("auth") auth: String,
        @PathVariable("id") idUser: Long,
        @PathVariable("idCity") idCity: Long
    ): ResponseEntity<Any>{

        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                val responseHeader = HttpHeaders()

                val userList = userService.loadUserDatesList(idUser,DateCityDTO(LocalDate.now().minusDays(1), idCity) )

                responseHeader.set("total", userList.size.toString())
                ResponseEntity(userList, responseHeader, HttpStatus.OK)
            }

        }  catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }

    }


    /**
     * Listen to a Post with the [Constants.URL_BASE_USER] and a requestBody with a [UserDTOInsert] to create an user
     *
     * @param user UserDTOInsert with the mandatory values for a new user
     * @return the id of the new user
     *
     * @exception ServiceException if there was any problem encrypting the password
     * @exception AlreadyExistsException if the nickname already exists
     */
    @PostMapping("")
    fun insertUser(@RequestBody user: UserDTOInsert): ResponseEntity<Any> {

        return try {

            val id = userService.save(user)
            val responseHeader = HttpHeaders()
            responseHeader.set("location", Constants.URL_BASE_USER + "/" + id)

            ResponseEntity(responseHeader, HttpStatus.CREATED)

        } catch (e: ServiceException) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: AlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.ALREADY_REPORTED)
        }
    }


    /**
     * Listen to a Patch with the [Constants.URL_BASE_USER] and a requestBody with a [UserDTOEdit] to update a User
     *
     * @param idUser id of the user that will be updated
     * @param user attributes to modify
     *
     * No nickname changes
     */
    @PatchMapping("/{id}")
    fun updateUser(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody user: UserDTOEdit
    ): ResponseEntity<Any> {

        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                userService.update(idUser, user)
                ResponseEntity(HttpStatus.OK)
            }

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Listen to a Delete with the [Constants.URL_BASE_USER] and a Id as a parameter to delete an user
     *
     * @param idUser id of the user that will be removed
     * @exception NotLoggedException if there is no token on the server's hashmap
     */
    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {
        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                userService.remove(idUser)
                ResponseEntity(HttpStatus.NO_CONTENT)
            }
        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
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
     * @exception NotLoggedException if there is no token on the server's hashmap
     *
     * Checks that the idUser and the nextCityId exists, if they don't it returns an HTTPResponse with the code [HttpStatus.NOT_FOUND]
     * otherwise will try to update it. In case the date already existed it will respond with a [HttpStatus.ALREADY_REPORTED]
     */
    @PutMapping("/{id}/date")
    fun addDate(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody dateCity: DateCityDTO
    ): ResponseEntity<Any> {

        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                if (!userService.exists(idUser))
                    ResponseEntity("No user with id $idUser found", HttpStatus.NOT_FOUND)
                else {
                    val id = dateCityService.addDate(idUser, dateCity)
                    val responseHeader = HttpHeaders()
                    responseHeader.set("id", id.toString())
                    ResponseEntity(true, responseHeader, HttpStatus.OK)
                }
            }

        } catch (e: AlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.ALREADY_REPORTED)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a DELETE with a [idDate] and an User ID, then deletes the DateCity from the db
     *
     * @param idUser id of the user that will be updated
     * @param dateCity data of the date
     *
     * @exception NotFoundException when the [idDate] doesn't match any db entry
     * @exception NoAuthorizationException when the [idUser] who sent the request is not the owner of the [idDate]
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     *
     * Try to delete it, if there is no problem it will return [HttpStatus.NO_CONTENT] or if the [idDate] doesn't exist
     * it will return [HttpStatus.NOT_FOUND] or if the [idUser] doesn't match with the owner of the [idDate]
     * it will return [HttpStatus.FORBIDDEN]
     */
    @DeleteMapping("/{id}/date")
    fun deleteDate(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody dateCity: DateCityDTO
    ): ResponseEntity<Any> {
        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                val responseHeader = HttpHeaders()

                val id = userService.deleteDate(idUser, dateCity)
                responseHeader.set("deleted",id.toString())

                ResponseEntity(true,  responseHeader ,HttpStatus.ACCEPTED)
            }

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * -----------------------------------------------------------
     *  Friendship related code starts here
     * -----------------------------------------------------------
     */

    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return A List<[UserFriendView]> with all the friendship of the user
     *
     */
    @GetMapping("/{id}/friends/users")
    fun getUsersFromFriendList(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {

        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                ResponseEntity(
                    friendshipService.listUsersFromFriendsByUser(idUser),
                    HttpStatus.OK
                )
            }
        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Post with a requestBody with a [FriendshipInsertDTO] to request a new [Friendship]
     *
     * @param friendship Two users' id pared as userAsk and userAnswer
     *
     * @exception NotFoundException when the [idUser] doesn't match any user or the [FriendshipInsertDTO.userAnswer] nickname doesn't exist
     * @exception AlreadyExistsException if the relationship already existed in any way
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     *
     */
    @PostMapping("/{id}/friends")
    fun insertRequestFriendShip(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody friendship: FriendshipInsertDTO
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {

                //Checks the user who made the post is the one who asks
                if (idUser == friendship.idUserAsk) {

                    val id = friendshipService.save(friendship)

                    responseHeader.set("location", "${Constants.URL_BASE_USER}/$idUser/chat/$id")
                    ResponseEntity(responseHeader, HttpStatus.CREATED)

                } else ResponseEntity("Error: User must be the one who asks", HttpStatus.FORBIDDEN)

            }

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: AlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.ALREADY_REPORTED)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Get with a [idUser] and [auth] for authorization
     *
     * @return returns all the friendships that are not accepted
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     *
     */
    @GetMapping("/{id}/friends")
    fun getFriendRequest(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {

        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else ResponseEntity(friendshipService.getFriendsRequest(idUser), HttpStatus.OK)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Patch with a [idUser] and [auth] for authorization and a RequestBody with [FriendshipUpdateDTO]
     *
     * Checks if the user and token are right, if they are checks if the friendship is accepted, if it is, then it's immutable,
     * if its not, then only the userAnswer can update the friendship.
     *
     * If the answer is yes, it will be saved and will only be possible to delete it, not modified it.
     * If the answer is no, it will be deleted.
     *
     * @return returns a message with information
     *
     * @exception NotFoundException when the [FriendshipUpdateDTO.id] doesn't match any friendships
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     *
     */
    @PatchMapping("/{id}/friends")
    fun updateRequest(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody friendRequest: FriendshipUpdateDTO
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        try {
            if (!securityCheck(idUser, auth))//if security fails
                return ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)

        } catch (e: NotLoggedException) {
            return ResponseEntity(e.message.toString(), HttpStatus.FORBIDDEN)
        }


        try {
            val friendRequestDB = friendshipService.load(friendRequest.id)

            //only non accepted requests can be updated
            if (friendRequestDB.getAnswer() == AnswerOptions.YES)
                return ResponseEntity("Friendship already accepted, can only be deleted", HttpStatus.FORBIDDEN)


            //Only the userAnswer can update the request
            if (idUser != friendRequestDB.getUserAnswer().getId())
                return ResponseEntity("Error: Only userAnswer can update the request", HttpStatus.FORBIDDEN)
            else {
                return when (friendRequest.answer) {
                    //Answer yes
                    AnswerOptions.YES -> {

                        friendshipService.update(friendRequest)
                        responseHeader.set("Friendship Id", "${friendRequest.id}")

                        ResponseEntity(responseHeader, HttpStatus.OK)

                    }
                    //answer no
                    AnswerOptions.NO -> {

                        //remove request
                        friendshipService.remove(friendRequestDB.getId())
                        ResponseEntity(responseHeader, HttpStatus.OK)

                    }
                    //any other answer
                    else -> {
                        ResponseEntity(HttpStatus.NO_CONTENT)
                    }
                }

            }


        } catch (e: NotFoundException) {
            return ResponseEntity(e.message.toString(), HttpStatus.NOT_FOUND)

        }

    }

    /**
     * Listen to a Delete with an [idFriends] to delete a friendship
     * //todo not sure how to handle this
     * @return The check of a deleted [Friendship]
     *
     * @exception NotFoundException if the [idFriends] doesn't match any friendship
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @DeleteMapping("/{id}/friends/{idFriends}")
    fun deleteFriendship(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("idFriends") idFriends: Long
    ): ResponseEntity<Any> {
        try {

            if (!securityCheck(idUser, auth))//if security fails
                return ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)

            val friends = friendshipService.load(idFriends)

            return if (friends.getUserAsk().getId() == idUser || friends.getUserAnswer().getId() == idUser) {
                friendshipService.remove(idFriends)

                ResponseEntity("Successfully delete ", HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity("Error: User is not in the friendship", HttpStatus.FORBIDDEN)
            }

        } catch (e: NotFoundException) {
            return ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            return ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * -----------------------------------------------------------
     *  Chat related code starts here
     * -----------------------------------------------------------
     */

    /**
     * Receives a idUser to show all the Friendships
     *
     * @return a [List<[Friendship]>] with all the friendship with any messages
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @GetMapping("/{idUser}/chat")
    fun getFriendshipWithMessages(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {

        return try {
            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else ResponseEntity(friendshipService.listUsersWithChatFromFriendsByUser(idUser), HttpStatus.OK)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_USER] with an [idUser] and [auth] for authentication and
     * [idFriendship] to show one friendship with messages
     *
     * @return a [Friendship] object with the specified [idFriendship] if the [idUser] is in that [Friendship]
     *
     * @exception NotFoundException when the [idFriendship] doesn't match any friendships
     * @exception NoAuthorizationException if the [idUser] is not in the friendship with id [idFriendship]
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @GetMapping("/{idUser}/chat/{idFriendship}")
    fun getChat(
        @PathVariable("idUser") idUser: Long,
        @PathVariable("idFriendship") idFriendship: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<Any> {

        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                val chat = messageService.getChat(idFriendship, idUser)
                ResponseEntity(chat, HttpStatus.OK)
            }

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NoAuthorizationException) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Listen to a Post with the [Constants.URL_BASE_USER] and a requestBody with a Message to create a Message
     * Save a message with the check that the [idUser] is in the [Friendship]
     *
     * @param msg new [Message] to insert in the database
     *
     * @exception NotFoundException if the [MessageForm.friendshipId] doesn't exist
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @PostMapping("/{idUser}/chat")
    fun insertMessage(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody msg: MessageForm
    ): ResponseEntity<Any> {
        try {
            val responseHeader = HttpHeaders()

            if (!securityCheck(idUser, auth))//if security fails
                return ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)

            //Load the friendship to check the user
            val friendsDB = friendshipService.load(msg.friendshipId)

            //If idUser is not on the friendship
            return if (friendsDB.getUserAsk().getId() != idUser && friendsDB.getUserAnswer().getId() != idUser)
                ResponseEntity("User must be in the friendship", HttpStatus.NOT_FOUND)
            else {
                val id = messageService.save(msg, idUser)
                responseHeader.set("location", Constants.URL_BASE_BAR + "/" + id)

                ResponseEntity(responseHeader, HttpStatus.CREATED)
            }


        } catch (e: NotFoundException) {
            return ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            return ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Listen to a Get with a [idUser] as id of the asking user,
     * [auth] as Header for authentication,
     * a formated date as [day]-[month]-[year],
     * and [idCity] to return the total number of people and number of friends
     * who checked that day in the same city
     *
     * @return a Response with two headers, "total" for total people, and "friends" for user's friends. Also a body with the events on that date
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @GetMapping("/{idUser}/{day}-{month}-{year}/{idCity}")
    fun getPeopleAndFriends(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("day") day: Int,
        @PathVariable("month") month: Int,
        @PathVariable("year") year: Int,
        @PathVariable("idCity") cityId: Long
    ): ResponseEntity<Any> {
        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {

                val st = StringBuilder()
                val dateString = st.append(day)
                    .append("-")
                    .append(month)
                    .append("-")
                    .append(year)

                val datePatter = Regex("\\d{1,2}-\\d{1,2}-\\d{4}")//todo improve cheking max 30, max 12,

                if (!dateString.matches(datePatter))
                    ResponseEntity("Date error, plase use date as  day-month-year", HttpStatus.UNAUTHORIZED)
                else {
                    val date = LocalDate.of(year, month, day)

                    if (!securityCheck(idUser, auth))//if security fails
                        ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
                    else {
                        val responseHeader = HttpHeaders()
                        val dateCity = DateCityDTO(date, cityId)

                        responseHeader.set("total", userService.getTotal(dateCity).toString())
                        responseHeader.set(
                            "friends",
                            friendshipService.getCountFriendsOnDate(idUser, dateCity).toString()
                        )



                        ResponseEntity(eventService.listEventByDayAndCity(date = date, idCity = cityId), responseHeader, HttpStatus.OK)
                    }
                }
            }


        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Get with a [idUser] as id of the asking user,
     * [auth] as Header for authentication,
     * a formated date as [day]-[month]-[year],
     * and [idCity] to return a list of [UserSnapProjection] with the friends of the user on the specified date and city
     *
     * @return a Response with a body with the a list of [UserSnapProjection]
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [tokenSimple]
     */
    @GetMapping("/{idUser}/{day}-{month}-{year}/{idCity}/users")
    fun getFriendsOnDate(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("day") day: Int,
        @PathVariable("month") month: Int,
        @PathVariable("year") year: Int,
        @PathVariable("idCity") cityId: Long
    ): ResponseEntity<Any> {

        return try {

            if (!securityCheck(idUser, auth))//if security fails
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)
            else {
                    val st = StringBuilder()
                    val dateString = st.append(day)
                        .append("-")
                        .append(month)
                        .append("-")
                        .append(year)

                    val datePatter = Regex("\\d{1,2}-\\d{1,2}-\\d{4}")

                    if (!dateString.matches(datePatter))
                        ResponseEntity("Date error, plase use date as  day-month-year", HttpStatus.UNAUTHORIZED)
                    else {

                        val dateCity = DateCityDTO(LocalDate.of(year, month, day), cityId)

                        val users = friendshipService.getFriendsOnDate(idUser, dateCity)

                        ResponseEntity(users, HttpStatus.OK)

                    }
                }


        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }


    }


    /**
     * Listen to a Get to return a list of [City] with all the cities on the DB
     *
     * @return a Response with a body with the a list of [City]
     */
    @GetMapping("/cities")
    fun getAllCities(): ResponseEntity<Any> {
        return ResponseEntity(cityService.list(), HttpStatus.OK)
    }

}