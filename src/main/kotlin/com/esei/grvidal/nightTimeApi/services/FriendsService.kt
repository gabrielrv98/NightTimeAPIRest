package com.esei.grvidal.nightTimeApi.services

import com.esei.grvidal.nightTimeApi.dto.FriendsInsertDTO
import com.esei.grvidal.nightTimeApi.dto.toFriendRequest
import com.esei.grvidal.nightTimeApi.repository.FriendsRepository
import com.esei.grvidal.nightTimeApi.repository.MessageRepository
import com.esei.grvidal.nightTimeApi.exception.AlreadyExistsException
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import com.esei.grvidal.nightTimeApi.exception.NotFoundException
import com.esei.grvidal.nightTimeApi.model.Friends
import com.esei.grvidal.nightTimeApi.model.Message
import com.esei.grvidal.nightTimeApi.projections.FriendProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendsBusiness
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class FriendsService : IFriendsBusiness {

    /**
     *Dependency injection with autowired
     */
    @Autowired
    lateinit var friendsRepository: FriendsRepository

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * Lists all the friendships of one User
     */
    private fun listFriendsByUser(userId: Long): List<FriendProjection> {

        return friendsRepository.findFriendsByUserAsk_IdOrUserAnswer_IdAndAnswer(userId, userId, AnswerOptions.YES)

    }

    /**
     * Lists all the users that are friend of one User
     */
    override fun listUsersFromFriendsByUser(userId: Long): List<UserProjection> {

        //empty list
        var list: List<UserProjection> = listOf()

        //Gets all the Friendships
        listFriendsByUser(userId)
            .forEach { friendProjection -> // Adds to the list the friend who aren't the user himself
                if (friendProjection.getUserAsk().getId() == userId)
                    list = list.plus(friendProjection.getUserAnswer())
                else if (friendProjection.getUserAnswer().getId() == userId)
                    list = list.plus(friendProjection.getUserAsk())
            }

        return list

    }


    /**
     * Lists all the chats of one User
     */
    @Throws(NotFoundException::class, ServiceException::class)
    override fun listMessagesFromChat(friendsId: Long): List<Message> {
        try {

            return messageRepository.findAllByFriends_Id(friendsId)

        } catch (e: NotFoundException) {
            throw NotFoundException(e.message)
        }
    }


    /**
     * This will show one Chat, if not, will throw a BusinessException or
     * if the object cant be found, it will throw a NotFoundException
     *
     * todo acabar
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun load(friendsId: Long): Friends {
        val op: Optional<Friends>
        try {
            op = friendsRepository.findById(friendsId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

        if (!op.isPresent) {
            throw NotFoundException("No se encontro al bar con el id $friendsId")
        }

        return op.get()

    }


    /**
     * This will save a new [Friends], if not, will throw an Exception
     */
    override fun save(friends: FriendsInsertDTO): Long {

        //Check if the relation already exists
        var op = friendsRepository.findUserAsk_IdAndUserAnswer_Id(
            friends.idUserAsk, friends.idUserAnswer
        )
        if (!op.isPresent) {

            //check if the opposite relation already exists
            op = friendsRepository.findUserAsk_IdAndUserAnswer_Id(
                friends.idUserAnswer, friends.idUserAsk
            )
            if (!op.isPresent) {

                //Check if the userAsk exists
                val userAsk = userRepository.findById(friends.idUserAsk)
                    .orElseThrow { NotFoundException("User who asks with id ${friends.idUserAsk} not found") }

                //Check if the userAnswer exists
                val userAnswer = userRepository.findById(friends.idUserAnswer)
                    .orElseThrow { NotFoundException("User who answers with id ${friends.idUserAnswer} not found") }


                return friendsRepository.save(
                    friends.toFriendRequest(userAsk, userAnswer)
                ).id
            }
        }
        throw AlreadyExistsException("Relation already exists")
    }

    /**
     * This will remove a CHAT through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     * //TODO It shouldn't be removed
     * todo check what happens if you do a .deletBy of a nonExist relation
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(friendsId: Long) {

        val op = friendsRepository.findById(friendsId)
            .orElseThrow { NotFoundException("No Friends with id $friendsId where found") }

        try {
            friendsRepository.deleteById(friendsId)
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }

    }

    /**
     * This will save a new Message, if not, will throw an Exception
     * Checks if the signed user is in the friends relationship
     */
    @Throws(ServiceException::class)
    override fun saveMsg(msg: Message): Message {
        try {
            val friends = msg.friends

            if (friends.userAsk != msg.user && friends.userAnswer != msg.user)
                throw ServiceException("User it's not on the friendship")
            else {
                return messageRepository.save(msg)
            }

        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }
}


















