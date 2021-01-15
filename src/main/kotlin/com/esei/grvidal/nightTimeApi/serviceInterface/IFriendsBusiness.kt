package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.FriendsInsertDTO
import com.esei.grvidal.nightTimeApi.dto.FriendsUpdateDTO
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IFriendsBusiness {


    //Lists all the users that are friend of one User //api propose
    fun listUsersFromFriendsByUser(userId: Long): List<UserProjection>


    //List all the messages in one chat of one user //api propose
    fun listMessagesFromChat(friendsId: Long): List<Message>

    //Show one chat
    fun load(friendsId: Long): Friends

    //Save a new FriendShip
    @Throws(NotFoundException::class, AlreadyExistsException::class)
    fun save(friends: FriendsInsertDTO): Long

    //Updates a  FriendShip
    @Throws(NotFoundException::class)
    fun update(friends: FriendsUpdateDTO)


    //remove a chat with cascade on delete //api propose
    fun remove(friendsId: Long)

    fun saveMsg(msg: Message): Message
}