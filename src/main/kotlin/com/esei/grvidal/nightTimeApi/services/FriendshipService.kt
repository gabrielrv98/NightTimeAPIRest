package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.FriendshipInsertDTO
import com.esei.grvidal.nightTimeApi.dto.FriendshipUpdateDTO
import com.esei.grvidal.nightTimeApi.dto.toFriendRequest
import com.esei.grvidal.nightTimeApi.repository.FriendshipRepository
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Friendship
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.ChatView
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import com.esei.grvidal.nightTimeApi.projections.UserFriendView
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendshipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/**
 * Friendship.kt service, is the implementation of the DAO interface
 *
 */
@Service
class FriendshipService : IFriendshipService {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var friendshipRepository: FriendshipRepository

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * Lists all the friendships of one User
     */
    private fun listFriendsByUser(userId: Long): List<FriendshipProjection> {
        return friendshipRepository.findFriendshipsFromUser( userId)
    }

    /**
     * Lists all the users that are friend of one User
     */
    override fun listUsersFromFriendsByUser(userId: Long): List<UserFriendView> {

        return  listFriendsByUser(userId).map{ UserFriendView(it,userId)}


    }

    /**
     * Lists all friendships with Messages from an User
     */
    override fun listUsersWithChatFromFriendsByUser(userId: Long): List<ChatView> {
        return friendshipRepository.getChatsWithMessages(userId).map { ChatView(it, userId) }
    }


    /**
     * Lists all the chats of one User
     */
    @Throws(NotFoundException::class, ServiceException::class)
    override fun listMessagesFromChat(friendsId: Long): List<Message> {
        try {

            return messageRepository.findAllByFriendship_Id(friendsId)

        } catch (e: NotFoundException) {
            throw NotFoundException(e.message)
        }
    }


    /**
     * This will show one Chat, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     *
     *
     */
    override fun load(friendsId: Long): FriendshipProjection {

        return friendshipRepository.findFriendshipById(friendsId)
            .orElseThrow { NotFoundException("Couldn't find relationship with id $friendsId")  }
    }


    /**
     * This will save a new [Friendship], if not, will throw an Exception
     */
    override fun save(friendship: FriendshipInsertDTO): Long {

        //Check if the relation already exists
        var op = friendshipRepository.findFriendshipByUserAsk_IdAndUserAnswer_Id(
            friendship.idUserAsk, friendship.idUserAnswer
        )
        if (!op.isPresent) {

            //check if the opposite relation already exists
            op = friendshipRepository.findFriendshipByUserAsk_IdAndUserAnswer_Id(
                friendship.idUserAnswer, friendship.idUserAsk
            )
            if (!op.isPresent) {

                //Check if the userAsk exists
                val userAsk = userRepository.findById(friendship.idUserAsk)
                    .orElseThrow { NotFoundException("User who asks with id ${friendship.idUserAsk} not found") }

                //Check if the userAnswer exists
                val userAnswer = userRepository.findById(friendship.idUserAnswer)
                    .orElseThrow { NotFoundException("User who answers with id ${friendship.idUserAnswer} not found") }


                return friendshipRepository.save(
                    friendship.toFriendRequest(userAsk, userAnswer)
                ).id
            }
        }
        throw AlreadyExistsException("Relationship already exists")
    }

    override fun update(friendshipParam: FriendshipUpdateDTO) {
        val friendship = friendshipRepository.findById(friendshipParam.id)
            .orElseThrow { NotFoundException("No friendship with id ${friendshipParam.id} were found") }
        friendship.answer = friendshipParam.answer
        friendshipRepository.save(friendship)
    }

    /**
     * This will remove a CHAT through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     * //TODO Should be marked as "deleted" instead of being deleted
     */
    override fun remove(friendsId: Long) {
        friendshipRepository.deleteById(friendsId)

    }

    /**
     * This will save a new Message, if not, will throw an Exception
     * Checks if the signed user is in the friendship relationship
     */
    @Throws(ServiceException::class)
    override fun saveMsg(msg: Message): Message {
            val friends = msg.friendship

            if (friends.userAsk != msg.user && friends.userAnswer != msg.user)
                throw ServiceException("User it's not on the friendship")
            else {
                return messageRepository.save(msg)
            }


    }
}


















