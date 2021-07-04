package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipDTOUpdate
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.*
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
import kotlin.jvm.Throws

/**
 * Service Interface for friendships
 */
interface IFriendshipService {


    //Lists all the users that are friend of one User //api propose
    fun listFriendsSnapByUser(userId: Long, page: Int, size: Int): List<FriendshipSnapView>

    //Lists all the users that have any message on the chat with the user
    fun listChatByUser(userId: Long): List<ChatView>

    //Show one FriendShip
    @Throws( NotFoundException::class)
    fun loadById(friendsId: Long): FriendshipProjection

    //Show one FriendShip
    @Throws( NotFoundException::class)
    fun loadByUsers(user1Id: Long, user2Id: Long): FriendshipProjection

    //Save a new FriendShip
    @Throws(NotFoundException::class, AlreadyExistsException::class)
    fun save(userAsk: Long, userAnswer: Long): Long

    //Updates a  FriendShip
    @Throws(NotFoundException::class, ServiceException::class)
    fun update(userId:Long, friendshipParam: FriendshipDTOUpdate)

    //remove a chat with cascade on delete //api propose
    fun remove(friendsId: Long)

    //Returns the number of friends of an user on a date on a place
    fun getCountFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): Int

    //Returns the friends preview of an user on a date on a place
    fun getFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO, page:Int, size:Int ): List<UserSnapView>

    //Returns a list with all the friendships not accepted yet
    fun getFriendsRequest(idUser: Long): List<UserFriendView>
    fun getCountFriendsRequest(idUser: Long): Int

    fun friendshipState(idUser1: Long, idUser2: Long): AnswerOptions
    fun listFriendshipsIds(idUser: Long): List<Long>
    fun getCountFriends(userId: Long): Int
}