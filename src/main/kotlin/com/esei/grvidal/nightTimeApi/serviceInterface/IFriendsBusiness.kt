package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection

/**
 * DAO Interface for Bars
 */
interface IFriendsBusiness {

    //List all the chats  //testing propose
    fun list(): List<Friends>

    //List all the chats of one user //api propose
    fun listUserByUser(userId: Long): List<UserProjection>

    fun listChatsByUser(userId: Long): List<Friends>
    fun listFriendsByUser(userId: Long): List<FriendProjection>

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