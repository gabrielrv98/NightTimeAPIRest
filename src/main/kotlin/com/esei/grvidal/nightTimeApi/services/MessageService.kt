package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.MessageForm
import com.esei.grvidal.nightTimeApi.repository.FriendshipRepository
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

/**
 * MessageService.kt service, is the implementation of the DAO interface
 *
 */
@Service
class MessageService : IMessageService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var friendshipRepository: FriendshipRepository

    @Autowired
    lateinit var messageRepository: MessageRepository

    /**
     * This will save a new [Message], if not, will throw an AlreadyExistsException
     */
    override fun save(msg: MessageForm, idUser: Long): Long {

        //Get the friendship
        val friendship = friendshipRepository.findById(msg.friendshipId)
            .orElseThrow { NotFoundException("Relationship with id $msg.friendshipId not found") }

        //adds the Message
        return messageRepository.save(
            Message(
                text = msg.text,
                date = LocalDate.now(),
                hour = LocalTime.now(),
                friendship = friendship,
                //get The Object User from the signed user
                user = if (idUser == friendship.userAnswer.id) friendship.userAnswer
                        else friendship.userAsk
            )
        ).id
    }

    override fun getChat(idFriendship: Long, idUser: Long): ChatView {

        //Get the friendship
        val friendship = friendshipRepository.findById(idFriendship)
            .orElseThrow { NotFoundException("Relationship with id $idFriendship.friendshipId not found") }

        return ChatView(friendship, idUser)
    }

}


















