package com.esei.grvidal.nightTimeApi.serviceInterface

import com.esei.grvidal.nightTimeApi.dto.MessageForm
import com.esei.grvidal.nightTimeApi.exception.NoAuthorizationException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.MessageView
import kotlin.jvm.Throws

/**
 * Service Interface for Message
 */
interface IMessageService {


    //Save a new Message
    @Throws(NotFoundException::class, NoAuthorizationException::class, ServiceException::class)
    fun save(msg: MessageForm, idUser: Long): MessageView

    @Throws(NotFoundException::class, NoAuthorizationException::class)
    fun getChat(idFriendship: Long, idUser: Long): ChatView

}