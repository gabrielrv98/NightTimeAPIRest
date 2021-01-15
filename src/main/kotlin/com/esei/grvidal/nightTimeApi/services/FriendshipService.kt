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
import com.esei.grvidal.nightTimeApi.projections.FriendshipProjection
import com.esei.grvidal.nightTimeApi.projections.UserProjection
import com.esei.grvidal.nightTimeApi.repository.UserRepository
import com.esei.grvidal.nightTimeApi.serviceInterface.IFriendshipBusiness
import com.esei.grvidal.nightTimeApi.utlis.AnswerOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

/**
 * Bar service, is the implementation of the DAO interface
 *
 */
@Service
class FriendshipService : IFriendshipBusiness {

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

        return friendshipRepository.findFriendshipByUserAsk_IdOrUserAnswer_IdAndAnswer(userId, userId, AnswerOptions.YES)

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

            return messageRepository.findAllByFriendship_Id(friendsId)

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
    @Throws( NotFoundException::class)
    override fun load(friendsId: Long): Friendship {


        return friendshipRepository.findById(friendsId)
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
        throw AlreadyExistsException("Relation already exists")
    }

    override fun update(friendshipParam: FriendshipUpdateDTO) {
        val friendship = friendshipRepository.findById(friendshipParam.id)
            .orElseThrow { NotFoundException("No friendship with id ${friendshipParam.id} were found") }
        friendship.answer = friendshipParam.answer
        friendshipRepository.save(friendship)
    }

    /**
     * This will remove a CHAT through its id, if not, will throw an Exception, or if it cant find it, it will throw a NotFoundException
     * //TODO It shouldn't be removed
     * todo check what happens if you do a .deletBy of a nonExist relation
     */
    @Throws(ServiceException::class, NotFoundException::class)
    override fun remove(friendsId: Long) {

        friendshipRepository.findById(friendsId)
            .orElseThrow { NotFoundException("No Friendship with id $friendsId were found") }

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


















