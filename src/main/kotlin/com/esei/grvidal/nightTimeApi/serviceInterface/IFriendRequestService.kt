package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.FriendRequestInsertDTO
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.FriendRequest
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IFriendRequestService {

    //List all the chats  //testing propose
    fun list(): List<FriendRequest>

    //List all the chats of one user //api propose
    @Throws( ServiceException::class)
    fun listByUserAnswer(userId: Long): List<FriendRequest>

    //Show one chat
    fun load(friendRequestId: Long): FriendRequest

    //Save a new chat //api propose
    @Throws( NotFoundException::class, AlreadyExistsException::class)
    fun save(friendRequest: FriendRequestInsertDTO): Long

    //remove a chat with cascade on delete //api propose
    fun remove(friendRequestId: Long)

}