package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.dto.*
import com.esei.grvidal.nightTimeApi.exception.*
import com.esei.grvidal.nightTimeApi.model.*
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.UserFriendView
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.serviceInterface.*
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import com.esei.grvidal.nightTimeApi.utlis.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

/**
 * This is the User Controller
 */
@RestController
@RequestMapping(Constants.URL_BASE_USER)
class UserRestController {

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var friendshipService: IFriendshipService

    @Autowired
    lateinit var dateCityService: IDateCityService

    @Autowired
    lateinit var cityService: ICityService

    @Autowired
    lateinit var messageService: IMessageService

    private val tokenSimple : HashMap<Long,String> = hashMapOf()

    @Throws( NotLoggedException::class )
    private fun securityCheck(id: Long, token: String): Boolean{
        tokenSimple.get(id)?.let{
            return it == token
        }
        throw NotLoggedException("No token associated with this user id $id")

    }

    /**
     * TEST
     */
    @PutMapping("/hash/{idUser}/{secret}")
    fun putHash(@PathVariable("idUser") idUser: Long,
                @PathVariable("secret") secret: String): ResponseEntity<Any> {

        tokenSimple[idUser] = secret
        return ResponseEntity(true, HttpStatus.OK)

    }

    /**
     * TEST
     */
    @GetMapping("/hash/{idUser}/")
    fun getHash(@PathVariable("idUser") idUser: Long): ResponseEntity<Any> {

        return ResponseEntity(tokenSimple[idUser], HttpStatus.OK)

    }



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
        @RequestHeader(name = "username") username: String,
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
    fun getUsersFromFriendList(@PathVariable("id") idUser: Long): ResponseEntity<List<UserFriendView>> {
        return ResponseEntity(
            friendshipService.listUsersFromFriendsByUser(idUser),
            HttpStatus.OK)
    }

    /**
     * Listen to a Post with a requestBody with a [FriendshipInsertDTO] to request a new [Friendship]
     *
     * @param friendship Two users' id pared as userAsk and userAnswer
     *
     */
    @PostMapping("/{id}/friends")
    fun insertRequestFriendShip(
        @PathVariable("id") idUser: Long,
        @RequestBody friendship: FriendshipInsertDTO
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        return try {

            //Checks the user who made the post is the one who asks
            if (idUser == friendship.idUserAsk) {

                val id = friendshipService.save(friendship)

                responseHeader.set("location", "${Constants.URL_BASE_USER}/$idUser/chat/$id")
                ResponseEntity(responseHeader, HttpStatus.CREATED)

            } else {
                ResponseEntity("Error: User must be the one who asks", HttpStatus.FORBIDDEN)
            }

        }catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: AlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.ALREADY_REPORTED)
        }
    }

    @GetMapping("/{id}/friends")
    fun getFriendRequest(
        @PathVariable("id") idUser: Long
        //, @RequestHeader("auth") auth: String
    ): ResponseEntity<Any>{

        //checking token todo quitar comentarios auth
        //if(!securityCheck(idUser,auth))
        //        ResponseEntity("Security error, credentials don't match",HttpStatus.UNAUTHORIZED)

        return ResponseEntity(friendshipService.getFriendsRequest(idUser),HttpStatus.OK)

    }
    /**
     * Returns all the friendships where the answer is 0 ( not answered)
     */

    /**
     * Lists all the users that where the friendship answer is [answer], by default [answer] is 1 (YES) ( they are friends)
     */

    @PatchMapping("/{id}/friends")
    fun updateRequest(
        @PathVariable("id") idUser: Long,
        @RequestBody friendRequest: FriendshipUpdateDTO
    ): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()

        try {
            val friendRequestDB = friendshipService.load( friendRequest.id)

            //only non accepted requests can be updated
            if(friendRequestDB.getAnswer() == AnswerOptions.YES)
                return ResponseEntity("Friendship already accepted, can only be deleted", HttpStatus.FORBIDDEN)


            //check the idUser is the user who can update the Request
            if (idUser == friendRequestDB.getUserAnswer().getId() ) {

                //Answer yes
                when (friendRequest.answer) {
                    AnswerOptions.YES -> {

                        friendshipService.update(friendRequest)
                        responseHeader.set("Friendship Id", "${friendRequest.id}")

                        return ResponseEntity(responseHeader, HttpStatus.OK)

                    }
                    //answer no
                    AnswerOptions.NO -> {

                        //remove request
                        friendshipService.remove(friendRequestDB.getId())
                        return ResponseEntity(responseHeader, HttpStatus.OK)

                    }
                    else -> {
                        return ResponseEntity(HttpStatus.ALREADY_REPORTED)
                    }
                }

            }
            //User has no permission
            return ResponseEntity("Error: Only userAnswer can update the request", HttpStatus.FORBIDDEN)


        } catch (e: NotFoundException) {
            return ResponseEntity(e.message,HttpStatus.NOT_FOUND)

        }catch (e: AlreadyExistsException) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    /**
     * Listen to a Post with the [Constants.URL_BASE_BAR] and a requestBody with a Bar to create a bar
     * //todo not sure how to handle this
     * @return The check of a deleted [Friendship]
     */
    @DeleteMapping("/{id}/friends/{idFriends}")
    fun deleteFriendship(
        @PathVariable("id") idUser: Long,
        @PathVariable("idFriends") idFriends: Long
    ): ResponseEntity<Any> {
        return try {

            val friends = friendshipService.load(idFriends)

            if (friends.getUserAsk().getId() == idUser || friends.getUserAnswer().getId() == idUser) {
                friendshipService.remove(idFriends)

                ResponseEntity("Successfully delete ",HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity("Error: User is not in the friendship", HttpStatus.FORBIDDEN)
            }

        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
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
     */
    @GetMapping("/{idUser}/chat")
    fun getFriendshipWithMessages(@PathVariable("idUser") idUser: Long): ResponseEntity<List<ChatView>> {
        return ResponseEntity(
            friendshipService.listUsersWithChatFromFriendsByUser(idUser),
            HttpStatus.OK
        )
    }


    /**
     * Listen to a Get with the [Constants.URL_BASE_BAR] and an Id as a parameter to show one Bar
     *
     * @return a [Friendship] object with the specified [idFriendship] if the [idUser] is in that [Friendship]
     */
    @GetMapping("/{idUser}/chat/{idFriendship}")
    fun getChat(
        @PathVariable("idUser") idUser: Long,
        @PathVariable("idFriendship") idFriendship: Long,
        @RequestHeader("auth") token: String
    ): ResponseEntity<Any> {

        return try {

        //checking token todo quitar comentarios
        //if(!securityCheck(idUser,auth))
        //        ResponseEntity("Security error, credentials don't match",HttpStatus.UNAUTHORIZED)


            //Security checks. If the idUser belongs to any user of the chat
            val friends = friendshipService.load(idFriendship)
            if (friends.getUserAsk().getId() != idUser && friends.getUserAnswer().getId() != idUser)
                ResponseEntity(HttpStatus.NOT_FOUND)

            else {
                ResponseEntity(messageService.getChat(idFriendship, idUser), HttpStatus.OK)
            }

        } catch (e: ServiceException) {
            ResponseEntity(e.message,HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: NotFoundException) {
            ResponseEntity(e.message,HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException){
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Listen to a Post with the [Constants.URL_BASE_USER] and a requestBody with a Message to create a Message
     * Save a message with the check that
     *      the [idUser] is in the [Friendship]
     *      the user who signed the message is on the relation
     *
     * @param msg new [Message] to insert in the database
     */
    @PostMapping("/{idUser}/chat")
    fun insertMessage(
        @PathVariable("idUser") idUser: Long,
        //@RequestHeader("auth") auth: String,
        @RequestBody msg: MessageForm
    ): ResponseEntity<Any> {
        return try {
            val responseHeader = HttpHeaders()

            //checking token todo quitar comentarios auth
            //if(!securityCheck(idUser,auth))
            //        ResponseEntity("Security error, credentials don't match",HttpStatus.UNAUTHORIZED)

            //Load the friendship to check the user
            val friendsDB   = friendshipService.load(msg.friendshipId)

            //If the user who signed the text, is not on the friendship
            if (friendsDB.getUserAsk().getId() != idUser && friendsDB.getUserAnswer().getId() != idUser)
                ResponseEntity("User must be in the friendship", HttpStatus.NOT_FOUND)
            else {
                val id = messageService.save(msg, idUser)
                responseHeader.set("location", Constants.URL_BASE_BAR + "/" + id)

                ResponseEntity(responseHeader, HttpStatus.CREATED)
            }



        } catch (e: NotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException){
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }





    /**
     * Listen to a Get with a [DateCityDTO] and an IdUser to return the total number of people and number of friends
     * who checked that day in the same city
     *
     * ADVICE: This method doesn't work with Swagger because Get request with bodies are not allowed
     *
     * @return a Response with two headers, "total" for total people, and "friends" for user's friends
     */
    @GetMapping("/{idUser}/date")
    fun getPeopleAndFriends(
            @PathVariable("idUser") idUser: Long,
            //@RequestHeader("auth") auth: String,
            @RequestBody dateCity: DateCityDTO,
    ): ResponseEntity<Any> {
        return try {
            //checking token todo quitar comentarios auth
            //if(!securityCheck(idUser,auth))
            //        ResponseEntity("Security error, credentials don't match",HttpStatus.UNAUTHORIZED)

            val responseHeader = HttpHeaders()

            if(userService.exists(idUser)) {

                responseHeader.set("total", userService.getTotal(dateCity).toString())
                responseHeader.set("friends", friendshipService.getFriendsOnDate(idUser, dateCity).toString())

                ResponseEntity(null, responseHeader, HttpStatus.OK)

            }else ResponseEntity("No user with id $idUser were found", HttpStatus.NOT_FOUND)


        }  catch (e: NotLoggedException){
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }



}