package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection

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

    //Save a new chat //api propose
    fun save(friends: Friends): Friends

    //remove a chat with cascade on delete //api propose
    fun remove(friendsId: Long)

    fun saveMsg(msg: Message): Message
}