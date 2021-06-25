package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipUpdateDTO
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.*
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
import kotlin.jvm.Throws

/**
 * DAO Interface for friendships
 */
interface IFriendshipService {


    //Lists all the users that are friend of one User //api propose
    fun listUsersFromFriendsByUser(userId: Long, page: Int, size: Int): List<FriendshipSnapProjection>

    //Lists all the users that have any message on the chat with the user
    fun listUsersWithChatFromFriendsByUser(userId: Long): List<ChatView>

    //Show one FriendShip
    @Throws( NotFoundException::class)
    fun load(friendsId: Long): FriendshipProjection

    //Show one FriendShip
    @Throws( NotFoundException::class)
    fun load(user1Id: Long, user2Id: Long): FriendshipProjection

    //Save a new FriendShip
    @Throws(NotFoundException::class, AlreadyExistsException::class)
    fun save(userAsk: Long, userAnswer: Long): Long

    //Updates a  FriendShip
    @Throws(NotFoundException::class)
    fun update(friendshipParam: FriendshipUpdateDTO)

    //remove a chat with cascade on delete //api propose
    fun remove(friendsId: Long)

    fun saveMsg(msg: Message): Message

    //Returns the number of friends of an user on a date on a place
    fun getCountFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): Int

    //Returns the friends preview of an user on a date on a place
    fun getFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO, page:Int, size:Int ): List<UserSnapProjection>

    //Returns a list with all the friendships not accepted yet
    fun getFriendsRequest(idUser: Long): List<UserFriendView>
    fun getCountFriendsRequest(idUser: Long): Int

    fun friendShipState(idUser1: Long, idUser2: Long): AnswerOptions
    fun listFriendshipsIds(idUser: Long): List<Long>
    fun getCountFriends(userId: Long): Int
}