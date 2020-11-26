package com.esei.grvidal.nightTimeApi.business

import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message

/**
 * DAO Interface for Bars
 */
interface IFriendsBusiness {

    //List all the chats  //testing propose
    fun list(): List<Friends>

    //List all the chats of one user //api propose
    fun listByUser(userId: Long): List<Friends>

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