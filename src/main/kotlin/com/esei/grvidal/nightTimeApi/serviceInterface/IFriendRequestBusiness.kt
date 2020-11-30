package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.FriendRequest
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IFriendRequestBusiness {

    //List all the chats  //testing propose
    fun list(): List<FriendRequest>

    //List all the chats of one user //api propose
    @Throws( ServiceException::class)
    fun listByUserAnswer(userId: Long): List<FriendRequest>

    //Show one chat
    fun load(friendRequestId: Long): FriendRequest

    //Save a new chat //api propose
    fun save(friendRequest: FriendRequest): FriendRequest

    //remove a chat with cascade on delete //api propose
    fun remove(friendRequestId: Long)

}