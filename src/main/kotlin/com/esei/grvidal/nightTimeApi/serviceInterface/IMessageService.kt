package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.MessageForm
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.projections.ChatView
import kotlin.jvm.Throws

/**
 * DAO Interface for Bars
 */
interface IMessageService {


    //Save a new Message
    @Throws(NotFoundException::class, AlreadyExistsException::class)
    fun save(msg: MessageForm, idUser: Long): Long

    @Throws(NotFoundException::class)
    fun getChat(idFriendship: Long, idUser: Long): ChatView

}