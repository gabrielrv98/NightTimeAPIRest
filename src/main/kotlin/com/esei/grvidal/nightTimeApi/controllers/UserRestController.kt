package com.esei.grvidal.nightTimeApi.controllers

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipDTOUpdate
import com.esei.grvidal.nightTimeApi.dto.UserDTOEdit
import com.esei.grvidal.nightTimeApi.dto.UserDTOInsert
import com.esei.grvidal.nightTimeApi.exception.*
import com.esei.grvidal.nightTimeApi.model.City
import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.projections.*
import com.esei.grvidal.nightTimeApi.serviceInterface.*
import com.esei.grvidal.nightTimeApi.services.PhotoType
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
import com.esei.grvidal.nightTimeApi.utils.Constants
import com.esei.grvidal.nightTimeApi.utils.Constants.Companion.ERROR_HEADER_TAG
import com.esei.grvidal.nightTimeApi.utils.TokenSimple.TokenSimple
import com.esei.grvidal.nightTimeApi.utils.TokenSimple.TokenSimple.securityCheck
import com.esei.grvidal.nightTimeApi.utils.TokenSimple.TokenSimple.tokenGen
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime


/**
 * This is the User Controller
 *
 */
@RestController
@RequestMapping(Constants.URL_BASE_USER)
class UserRestController {

    private val logger = LoggerFactory.getLogger(NightTimeApiApplication::class.java)!!

    //Service injections
    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var friendshipService: IFriendshipService

    @Autowired
    private lateinit var dateCityService: IDateCityService

    @Autowired
    private lateinit var messageService: IMessageService

    @Autowired
    private lateinit var cityService: ICityService

    @Autowired
    private lateinit var eventService: IEventService

    @Autowired
    private lateinit var storeService: IStoreService


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
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()
        return try {
            val idUser = userService.login(username, password)

            if (idUser != -1L) {

                val token = tokenGen(25) //max users online: 25 char * 71 charset = 1775 users

                TokenSimple[idUser] = token
                responseHeader.set("id", idUser.toString())
                responseHeader.set("token", token)
                logger.info("user $idUser logged in successfully")
                ResponseEntity(true, responseHeader, HttpStatus.ACCEPTED)


            } else {

                responseHeader.set(ERROR_HEADER_TAG, "Credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.BAD_REQUEST)
            }


        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: ServiceException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

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
    ): ResponseEntity<String> {

        return try {

            if (securityCheck(idUser, auth)) {
                TokenSimple.remove(idUser)
                ResponseEntity("Good bye", HttpStatus.ACCEPTED)

            } else
                ResponseEntity("Security error, credentials don't match", HttpStatus.UNAUTHORIZED)

        } catch (e: NotLoggedException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Receives an [searchedUserId] and returns a UserProjection
     *
     * @exception NotFoundException the [searchedUserId] is wrong [HttpStatus.NOT_FOUND] will be shown
     */
    @GetMapping("/{idSearchedUser}")
    fun loadProjection(
        @PathVariable("idSearchedUser") searchedUserId: Long,
        @RequestHeader("auth") auth: String,
        @RequestHeader("clientUser") clientId: Long
    ): ResponseEntity<UserProfileView?> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(clientId, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(null, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                val user = UserProfileView(
                    user = userService.loadProjection(searchedUserId),
                    friendshipState = if (searchedUserId != clientId) //If the user is not looking for himself check if they are friends
                        friendshipService.friendshipState(searchedUserId, clientId)
                    else AnswerOptions.NO
                )

                if (user.friendshipState != AnswerOptions.YES) //If users are not friends hide private fields projection
                    user.nextDate = null


                ResponseEntity(user, HttpStatus.OK)
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
            ResponseEntity(null, responseHeader, HttpStatus.FORBIDDEN)

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
            ResponseEntity(null, responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Receives an [idUser] and returns a UserProjection
     *
     * @exception NotFoundException the [idUser] is wrong [HttpStatus.NOT_FOUND] will be shown
     */
    @GetMapping("/{id}/private")
    fun loadPrivateProjection(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<UserDTOEdit?> {
        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(null, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                ResponseEntity(userService.loadEditProjection(idUser), HttpStatus.OK)
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
            ResponseEntity(null, responseHeader, HttpStatus.FORBIDDEN)

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
            ResponseEntity(null, responseHeader, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{id}/photo")
    fun getPicture(
        @PathVariable("id") id: Long
    ): ResponseEntity<ByteArray?> {
        val responseHeader = HttpHeaders()
        return try {

            val userPic = userService.getPicture(id)
            logger.info("fetching image $userPic")

            userPic?.let { dir ->
                logger.info("dir = $dir")
                try {
                    val inputStream: InputStream = javaClass
                        .getResourceAsStream(dir)
                    val photo = inputStream.readBytes()
                    inputStream.close()

                    ResponseEntity(photo, HttpStatus.OK)

                } catch (e: NullPointerException) {
                    logger.error("Error getting image $e")
                    responseHeader.set(ERROR_HEADER_TAG, "Error getting image $e")
                    ResponseEntity(null, responseHeader, HttpStatus.NOT_FOUND)
                }

            } ?: ResponseEntity(
                null,
                responseHeader
                    .apply { this.set(ERROR_HEADER_TAG, "User has no image") },
                HttpStatus.NOT_FOUND
            )


        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(null, responseHeader, HttpStatus.NOT_FOUND)
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
    ): ResponseEntity<List<DateCityReducedProjection>> {
        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {


                val userList = userService.loadUserDatesList(idUser, DateCityDTO(LocalDate.now().minusDays(1), idCity))

                ResponseEntity(userList, responseHeader, HttpStatus.OK)
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
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
    fun insertUser(
        @RequestBody user: UserDTOInsert
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        return try {

            val id = userService.save(user)
            responseHeader.set("id", Constants.URL_BASE_USER + "/" + id)

            val token = tokenGen(25) //max users online: 25 char * 71 charset = 1775 users

            TokenSimple[id] = token
            responseHeader.set("id", id.toString())
            responseHeader.set("token", token)

            ResponseEntity(true, responseHeader, HttpStatus.CREATED)

        } catch (e: ServiceException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            logger.info("exception insertUser ServiceException $e")
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: AlreadyExistsException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            logger.info("exception insertUser AlreadyExistsException ${e.message.toString()}")
            ResponseEntity(false, responseHeader, HttpStatus.ALREADY_REPORTED)
        }
    }

    /**
     * Receives an img as [MultipartFile] and saves the new file in /target/userpics/user_userId_LocalTime
     * Also removes the old image. This is because Spring saves cache space for the resources dir, so if the
     * image its not deleted it will show the old picture
     */
    @PostMapping("/{idUser}/picture")
    fun setPicture(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestParam img: MultipartFile
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                logger.info("filename original ${img.originalFilename}")

                val user = userService.loadProjection(idUser)

                val oldName = user.getPicture()

                //save image in directory
                val newName = storeService.store(
                    img,
                    "user_${user.getNickname()}_${LocalDate.now()}_${LocalTime.now()}.jpg",
                    PhotoType.userPhoto
                )
                //delete old image
                oldName?.let {
                    storeService.delete(it)
                }
                //save  reference to picture
                logger.info("New file name $newName, user $idUser")
                userService.setUserPicture(idUser, newName)
                ResponseEntity(true, HttpStatus.OK)
            }

        } catch (e: NotFoundException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: ServiceException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: NotLoggedException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
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
    ): ResponseEntity<Boolean> {

        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                if (idUser != user.id) {
                    responseHeader.set(ERROR_HEADER_TAG, "User $idUser, has no rights to edit user ${user.id}")
                    ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
                } else {
                    userService.update(idUser, user)
                    ResponseEntity(true, HttpStatus.OK)
                }
            }

        } catch (e: NotFoundException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR)

        } catch (e: NotLoggedException) {

            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
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
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                userService.remove(idUser)
                ResponseEntity(true, HttpStatus.NO_CONTENT)
            }
        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
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
    ): ResponseEntity<Boolean> {

        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                val id = dateCityService.addDate(idUser, dateCity)
                responseHeader.set("id", id.toString())
                ResponseEntity(true, responseHeader, HttpStatus.OK)

            }

        } catch (e: AlreadyExistsException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.ALREADY_REPORTED)

        } catch (e: ServiceException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a DELETE with a [DateCityDTO] and an User ID, then deletes the DateCity from the db
     *
     * @param idUser id of the user that will be updated
     * @param dateCity data of the date
     *
     * @exception NotFoundException when the [DateCityDTO] doesn't match any db entry
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     *
     * Try to delete it, if there is no problem it will return true, id, [HttpStatus.ACCEPTED],
     * if the [dateCity] doesn't exist it will return [HttpStatus.NOT_FOUND]
     * or if the [idUser] doesn't match with the owner of the [dateCity]
     * it will return [HttpStatus.FORBIDDEN]
     */
    @DeleteMapping("/{id}/date")
    fun deleteDate(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody dateCity: DateCityDTO
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                val id = userService.deleteDate(idUser, dateCity)
                responseHeader.set("deleted", id.toString())

                ResponseEntity(true, responseHeader, HttpStatus.ACCEPTED)
            }

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
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
     *
     * @return A List<[UserSnapView]> with all the friendship of the user with friendshipId as userId
     *
     */
    @GetMapping("/{id}/friends/users")
    fun getUsersFromFriendList(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "17") size: Int
     ): ResponseEntity<List<FriendshipSnapView>> {

        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                responseHeader.set("total",friendshipService.getCountFriends(idUser).toString())
                ResponseEntity(
                    friendshipService.listFriendsSnapByUser(idUser,page,size),
                    responseHeader,
                    HttpStatus.OK
                )
            }
        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
        }
    }

    @GetMapping("/search/{nickname}")
    fun searchUser(
        @PathVariable("nickname") userString: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "17") size: Int
    ): ResponseEntity<List<UserSnapView>> {

        val responseHeader = HttpHeaders()

        responseHeader.set("total", userService.countUsersByString(userString = userString).toString())

        return ResponseEntity(
            userService.searchUsersByString(
                userString = userString, page = page, size = size
            ),
            responseHeader,
            HttpStatus.OK
        )

    }

    /**
     * Listen to a Post with an user logged and an ID of other user to request a new [Friendship]
     *
     * @param idUser User who request the friendship
     * @param idFriend User who is requested friendship
     *
     * @exception NotFoundException when the [idFriend] doesn't match any user
     * @exception AlreadyExistsException if the relationship already existed in any way
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     *
     */
    @PostMapping("/{id}/friends/{idFriend}")
    fun insertRequestFriendShip(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("idFriend") idFriend: Long
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                val id = friendshipService.save(idUser, idFriend)

                responseHeader.set("location", "${Constants.URL_BASE_USER}/$idUser/chat/$id")
                ResponseEntity(true, responseHeader, HttpStatus.CREATED)
            }

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)

        } catch (e: AlreadyExistsException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.ALREADY_REPORTED)

        }
    }

    /**
     * Listen to a Get with a [idUser] and [auth] for authorization
     *
     * @return returns all the friendships that are not accepted
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     *
     */
    @GetMapping("/{id}/friends")
    fun getFriendRequest(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<List<UserFriendView>> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {
                //if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {


                responseHeader.set("total", friendshipService.getCountFriendsRequest(idUser).toString())

                ResponseEntity(
                    friendshipService.getFriendsRequest(idUser),
                    responseHeader,
                    HttpStatus.OK
                )
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Patch with a [idUser] and [auth] for authorization and a RequestBody with [FriendshipDTOUpdate]
     *
     * Checks if the user and token are right, if they are checks if the friendship is accepted, if it is, then it's immutable,
     * if its not, then only the userAnswer can update the friendship.
     *
     * If the answer is yes, it will be saved and will only be possible to delete it, not modified it.
     * If the answer is no, it will be deleted.
     *
     * @return returns a message with information
     *
     * @exception NotFoundException when the [FriendshipDTOUpdate.id] doesn't match any friendships
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     *
     */
    @PatchMapping("/{id}/friends")
    fun updateRequest(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @RequestBody friendRequest: FriendshipDTOUpdate
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()

        try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                return ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            }

            val friendRequestDB= friendshipService.loadById(friendRequest.id)

            //only non accepted requests can be updated
            if (friendRequestDB.getAnswer() != AnswerOptions.NOT_ANSWERED) {
                responseHeader.set(ERROR_HEADER_TAG, "Friendship already accepted, can only be deleted")
                return ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
            }


            //Only the userAnswer can update the request
            if (idUser != friendRequestDB.getUserAnswer().getId()) {
                responseHeader.set(ERROR_HEADER_TAG, "Error: Only userAnswer can update the request")
                return ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)
            } else {
                return when (friendRequest.answer) {
                    //Answer yes
                    AnswerOptions.YES -> {

                        friendshipService.update(friendRequest)
                        responseHeader.set("Friendship Id", "${friendRequestDB.getId()}")

                        ResponseEntity(true, responseHeader, HttpStatus.OK)

                    }
                    //answer no
                    AnswerOptions.NO -> {

                        //remove request
                        friendshipService.remove(friendRequestDB.getId())
                        ResponseEntity(true, responseHeader, HttpStatus.OK)

                    }
                    //any other answer
                    else -> {
                        ResponseEntity(HttpStatus.NO_CONTENT)
                    }
                }

            }


        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            return ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            return ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)

        }

    }

    /**
     * Listen to a Delete with an [idFriend] to delete a friendship
     * @return The check of a deleted [Friendship]
     *
     * @exception NotFoundException if the [idFriend] and [idUser] don't have any friendship
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     */
    @DeleteMapping("/{id}/friends/{idFriend}")
    fun deleteFriendship(
        @PathVariable("id") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("idFriend") idFriend: Long
    ): ResponseEntity<Boolean> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(false, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                val friends = friendshipService.loadByUsers(idUser, idFriend)

                friendshipService.remove(friends.getId())

                ResponseEntity(true, HttpStatus.OK)
            }

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(false, responseHeader, HttpStatus.FORBIDDEN)

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
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     */
    @GetMapping("/{idUser}/chat")
    fun getFriendshipWithMessages(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<List<ChatView>> {
        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                logger.info("user $idUser is cheking his chats")
                ResponseEntity(
                    friendshipService
                        .listChatByUser(idUser)
                        .sortedByDescending { it.messages[0].date }
                        .sortedByDescending { it.messages[0].time },
                    HttpStatus.OK
                )
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message.toString())
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
        }
    }


    /**
     * Returns all the ids of the friendships where there user is involved
     *
     */
    @GetMapping("/{idUser}/friends/id")
    fun getFriendshipsIds(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<List<Long>> {

        val responseHeader = HttpHeaders()
        return try {
            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                ResponseEntity(
                    friendshipService
                        .listFriendshipsIds(idUser),
                    HttpStatus.OK
                )
            }

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message.toString())
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
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
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     */
    @GetMapping("/{idUser}/chat/{idFriendship}")
    fun getChat(
        @PathVariable("idUser") idUser: Long,
        @PathVariable("idFriendship") idFriendship: Long,
        @RequestHeader("auth") auth: String
    ): ResponseEntity<ChatView?> {

        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(null, responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                val chat = messageService.getChat(idFriendship, idUser)
                ResponseEntity(chat, HttpStatus.OK)
            }

        } catch (e: NotFoundException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message.toString())
            ResponseEntity(null, responseHeader, HttpStatus.NOT_FOUND)

        } catch (e: NoAuthorizationException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message.toString())
            ResponseEntity(null, responseHeader, HttpStatus.UNAUTHORIZED)

        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message.toString())
            ResponseEntity(null, responseHeader, HttpStatus.FORBIDDEN)
        }
    }




    /**
     * Listen to a Get with a [idUser] as id of the asking user,
     * [auth] as Header for authentication,
     * a formatted date as [day]-[month]-[year],
     * and [cityId] to return the total number of people and number of friends
     * who checked that day in the same city
     *
     * @return a Response with two headers, "total" for total people, and "friends" for user's friends. Also a body with the events on that date
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     */
    @GetMapping("/{idUser}/{day}-{month}-{year}/{idCity}")
    fun getPeopleAndFriends(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("day") day: Int,
        @PathVariable("month") month: Int,
        @PathVariable("year") year: Int,
        @PathVariable("idCity") cityId: Long
    ): ResponseEntity<List<EventProjection>> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {

                val st = StringBuilder()
                val dateString = st.append(day)
                    .append("-")
                    .append(month)
                    .append("-")
                    .append(year)


                val datePatter =
                    Regex("(((0?[1-9]|[1-2][0-9]|3[0-1])-(0?[13578]|(10|12)))|((0?[1-9]|[1-2][0-9])-0?2)|((0?[1-9]|[1-2][0-9]|30)-(0?[469]|11)))-[0-9]{4}")


                if (!dateString.matches(datePatter)) {
                    responseHeader.set(ERROR_HEADER_TAG, "Date error, please use date as  day-month-year")
                    ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
                } else {
                    try {
                        val date = LocalDate.of(year, month, day)

                        val dateCity = DateCityDTO(date, cityId)

                        responseHeader.set("total", userService.getTotal(dateCity).toString())
                        responseHeader.set(
                            "friends",
                            friendshipService.getCountFriendsOnDate(idUser, dateCity).toString()
                        )



                        ResponseEntity(
                            eventService.listEventByDayAndCity(date = date, idCity = cityId),
                            responseHeader,
                            HttpStatus.OK
                        )


                    } catch (e: DateTimeException) {
                        responseHeader.set(
                            ERROR_HEADER_TAG,
                            "Date error, please use a valid date, from 1-31 days and 1-12 month"
                        )
                        ResponseEntity(
                            listOf(),
                            responseHeader,
                            HttpStatus.UNAUTHORIZED
                        )
                    }


                }
            }


        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
        }
    }

    /**
     * Listen to a Get with a [idUser] as id of the asking user,
     * [auth] as Header for authentication,
     * a formatted date as [day]-[month]-[year],
     * and [cityId] to return a list of [UserSnapView] with the friends of the user on the specified date and city
     *
     * @return a Response with a body with the a list of [UserSnapView]
     *
     * @exception NotLoggedException if the [idUser] is not in the hashMap [TokenSimple]
     */
    @GetMapping("/{idUser}/{day}-{month}-{year}/{idCity}/users")
    fun getFriendsOnDate(
        @PathVariable("idUser") idUser: Long,
        @RequestHeader("auth") auth: String,
        @PathVariable("day") day: Int,
        @PathVariable("month") month: Int,
        @PathVariable("year") year: Int,
        @PathVariable("idCity") cityId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "17") size: Int
    ): ResponseEntity<List<UserSnapView>> {
        val responseHeader = HttpHeaders()
        return try {

            if (!securityCheck(idUser, auth)) {//if authentication fails
                responseHeader.set(ERROR_HEADER_TAG, "Security error, credentials don't match")
                ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
            } else {
                val st = StringBuilder()
                val dateString = st.append(day)
                    .append("-")
                    .append(month)
                    .append("-")
                    .append(year)

                val datePatter =
                    Regex("(((0?[1-9]|[1-2][0-9]|3[0-1])-(0?[13578]|(10|12)))|((0?[1-9]|[1-2][0-9])-0?2)|((0?[1-9]|[1-2][0-9]|30)-(0?[469]|11)))-[0-9]{4}")

                if (!dateString.matches(datePatter)) {
                    responseHeader.set(ERROR_HEADER_TAG, "Date error, please use date as  day-month-year")
                    ResponseEntity(listOf(), responseHeader, HttpStatus.UNAUTHORIZED)
                } else {

                    val dateCity = DateCityDTO(LocalDate.of(year, month, day), cityId)

                    val users = friendshipService.getFriendsOnDate(idUser, dateCity, page, size)

                    logger.info("User $idUser, checking date $day-$month-$year city id $cityId, page $page users sent ${users.size}")
                    ResponseEntity(users, HttpStatus.OK)

                }
            }


        } catch (e: NotLoggedException) {
            responseHeader.set(ERROR_HEADER_TAG, e.message)
            ResponseEntity(listOf(), responseHeader, HttpStatus.FORBIDDEN)
        }


    }


    /**
     * Listen to a Get to return a list of [City] with all the cities on the DB
     *
     * @return a Response with a body with the a list of [City]
     */
    @GetMapping("/cities")
    fun getAllCities(): ResponseEntity<List<City>> {
        return ResponseEntity(cityService.list(), HttpStatus.OK)
    }

}