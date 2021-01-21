package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.DateCityDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipInsertDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipUpdateDTO
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import com.esei.grvidal.nightTimeApi.projections.UserFriendView
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IFriendshipService {


    //Lists all the users that are friend of one User //api propose
    fun listUsersFromFriendsByUser(userId: Long): List<UserFriendView>

    //Lists all the users that have any message on the chat with the user
    fun listUsersWithChatFromFriendsByUser(userId: Long): List<ChatView>


    //List all the messages in one chat of one user //api propose
    fun listMessagesFromChat(friendsId: Long): List<Message>

    //Show one chat
    @Throws( NotFoundException::class)
    fun load(friendsId: Long): FriendshipProjection

    //Save a new FriendShip
    @Throws(NotFoundException::class, AlreadyExistsException::class)
    fun save(friendship: FriendshipInsertDTO): Long

    //Updates a  FriendShip
    @Throws(NotFoundException::class)
    fun update(friendshipParam: FriendshipUpdateDTO)


    //remove a chat with cascade on delete //api propose
    fun remove(friendsId: Long)

    fun saveMsg(msg: Message): Message

    fun getFriendsOnDate(idUser: Long, dateCityDTO: DateCityDTO): Int
}