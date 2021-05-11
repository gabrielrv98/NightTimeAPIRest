package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.MessageForm
import com.esei.grvidal.nightTimeApi.exception.NoAuthorizationException
import com.esei.grvidal.nightTimeApi.repository.FriendshipRepository
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.MessageView
import com.esei.grvidal.nightTimeApi.serviceInterface.IMessageService
import com.esei.grvidal.nightTimeApi.utils.AnswerOptions
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
    override fun save(msg: MessageForm, idUser: Long): MessageView {

        //Get the friendship
        val friendship = friendshipRepository.findById(msg.friendshipId)
            .orElseThrow { NotFoundException("Relationship with id $msg. friendshipId not found") }

        if (friendship.userAnswer.id != idUser && friendship.userAsk.id != idUser) {
            throw NoAuthorizationException("User is not part of the friendship")

        }else if( friendship.answer != AnswerOptions.YES){
            throw ServiceException("Friendship must be accepted before chatting")
        }

        //adds the Message
        return MessageView(
            messageRepository.save(
                Message(
                    text = msg.text,
                    date = LocalDate.now(),
                    hour = LocalTime.now(),
                    friendship = friendship,
                    // Get the object [User] from the signed user
                    user = if (idUser == friendship.userAnswer.id) friendship.userAnswer
                    else friendship.userAsk
                )
            )
        )
    }

    override fun getChat(idFriendship: Long, idUser: Long): ChatView {

        //Get the friendship
        val friendship = friendshipRepository.findById(idFriendship)
            .orElseThrow { NotFoundException("Relationship with id $idFriendship.friendshipId not found") }

        if (friendship.userAnswer.id != idUser && friendship.userAsk.id != idUser)
            throw NoAuthorizationException("$idUser is not in the friendship and cannot see the messages")

        messageRepository.markAllAsReadByFriendshipId(idFriendship,idUser)

        return ChatView(
            friendship = friendship,
            userId = idUser,
            isSnap = false
        )
    }

}


















